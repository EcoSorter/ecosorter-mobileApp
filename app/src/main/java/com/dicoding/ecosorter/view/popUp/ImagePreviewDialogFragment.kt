package com.dicoding.ecosorter.view.popUp


import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dicoding.ecosorter.R

class ImagePreviewDialogFragment(private val listener: (selectedImageUri: Uri) -> Unit) : DialogFragment() {
    private var selectedImageUri: Uri? = null
    private lateinit var imageView: ImageView
    private lateinit var scanButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_image_preview_dialog, container, false)
        imageView = view.findViewById(R.id.imageView)
        scanButton = view.findViewById(R.id.scanButton)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load and display the selected image
        selectedImageUri?.let { uri ->
            Glide.with(requireContext())
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView)
        }

        // Handle scan button click
        scanButton.setOnClickListener {
            selectedImageUri?.let { it1 -> listener(it1) }
            dismiss()
        }
    }

    companion object {
        fun newInstance(imageUri: Uri, listener: (selectedImageUri: Uri) -> Unit): ImagePreviewDialogFragment {
            val fragment = ImagePreviewDialogFragment{
                listener(it)
            }
            fragment.selectedImageUri = imageUri
            return fragment
        }
    }
}
