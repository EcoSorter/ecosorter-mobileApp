package com.dicoding.ecosorter.view.camera

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.dicoding.ecosorter.databinding.FragmentCameraBinding
import com.dicoding.ecosorter.utils.createFile
import com.dicoding.ecosorter.view.popUp.ImagePreviewDialogFragment
import com.dicoding.ecosorter.view.popUp.ImageStatusActivity
import com.dicoding.ecosorter.view.popUp.postRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class CameraFragment : Fragment() {
    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null

    private lateinit var cameraViewModel: CameraViewModel

    private var loadingOverlay: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.captureImage.setOnClickListener { takePhoto() }

        binding.gallery.setOnClickListener{
            startGallery()
        }

        binding.switchCamera.setOnClickListener {
            cameraSelector = if(cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA else CameraSelector.DEFAULT_BACK_CAMERA
            startCamera()
        }

        cameraViewModel = ViewModelProvider(this)[CameraViewModel::class.java]

        cameraViewModel.isLoading.observe(this, {showLoading(it)})

        cameraViewModel.response.observe(viewLifecycleOwner) { responseResult ->
            val intent = Intent(activity, ImageStatusActivity::class.java)
            intent.putExtra("imageStatus", responseResult)
            activity?.startActivity(intent)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            showLoadingOverlay()
        } else {
            binding.progressBar.visibility = View.GONE
            hideLoadingOverlay()
        }

    }

    private fun showLoadingOverlay() {
        if (loadingOverlay == null) {
            loadingOverlay = View(requireContext()).apply {
                setBackgroundColor(Color.parseColor("#80000000")) // Semi-transparent black color
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }

            (activity?.window?.decorView as? ViewGroup)?.addView(loadingOverlay)
        }
    }

    private fun hideLoadingOverlay() {
        loadingOverlay?.let { overlay ->
            (activity?.window?.decorView as? ViewGroup)?.removeView(overlay)
            loadingOverlay = null
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg: Uri = result.data?.data ?: return@registerForActivityResult
            // Show the image preview dialog
            val imagePreviewDialog = ImagePreviewDialogFragment.newInstance(selectedImg){
                execute(it)
            }
            imagePreviewDialog.show(requireActivity().supportFragmentManager, "ImagePreviewDialog")

        }
    }

    private fun startGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val filePhoto = createFile(requireActivity().application)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(filePhoto).build()

        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(requireContext(), "Failed to capture image.", Toast.LENGTH_SHORT).show()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = output.savedUri ?: Uri.fromFile(filePhoto)

                    val dialogFragment = ImagePreviewDialogFragment.newInstance(savedUri){
                        execute(it)
                    }
                    dialogFragment.show(parentFragmentManager, "ImagePreviewDialog")
                }
            }
        )
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .apply {
                    setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Failed to launch camera", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun hideSystemUI(state: Boolean) {
        if (state) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                requireActivity().window.insetsController?.hide(WindowInsets.Type.statusBars())
            } else {
                @Suppress("DEPRECATION")
                requireActivity().window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
                )
            }
            (requireActivity() as? AppCompatActivity)?.supportActionBar?.hide()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                requireActivity().window.insetsController?.show(WindowInsets.Type.statusBars())
            } else {
                @Suppress("DEPRECATION")
                requireActivity().window.clearFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
                )
            }
            (requireActivity() as? AppCompatActivity)?.supportActionBar?.show()
        }
    }

    private fun execute(selectedImageUri: Uri){
            selectedImageUri.let { uri ->
                val inputStream = requireContext().contentResolver.openInputStream(uri)
                val byteArray = inputStream?.readBytes()

                if (byteArray != null) {
                    val base64Image = Base64.encodeToString(byteArray, Base64.NO_WRAP)
                    cameraViewModel.postImage(requireContext(),base64Image)
                }
                inputStream?.close()
            }
    }
    override fun onResume() {
        super.onResume()
        hideSystemUI(true)
        startCamera()
    }

    override fun onStop() {
        super.onStop()
        hideSystemUI(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): CameraFragment{
            val fragment = CameraFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}