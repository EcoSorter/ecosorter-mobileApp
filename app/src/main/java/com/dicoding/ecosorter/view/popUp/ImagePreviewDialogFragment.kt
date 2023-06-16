package com.dicoding.ecosorter.view.popUp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.dicoding.ecosorter.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.InputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL


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

fun postRequest(url: String, jsonBody: String): String {
    val connection = URL(url).openConnection() as HttpURLConnection
    connection.requestMethod = "POST"
    connection.setRequestProperty("Content-Type", "application/json")
    connection.doOutput = true
    var result = ""
    try {
        val outputStream = BufferedOutputStream(connection.outputStream)
        val writer = OutputStreamWriter(outputStream)
        writer.write(jsonBody)
        writer.flush()

        val responseCode = connection.responseCode

        outputStream.close()


        if (responseCode == 200){
            val inputStream = BufferedInputStream(connection.inputStream)
            val response = readResponse(inputStream)

            val jsonObject = JSONObject(response)
            result = jsonObject.getString("result")
        }

        return result
    } catch (e: Exception) {
        // Handle exceptions and display error messages
        Log.e("Error", "Failed to make the network request: ${e.message}")
    } finally {
        connection.disconnect()
    }
    return result
}

fun readResponse(inputStream: InputStream): String {
    return inputStream.bufferedReader().use { it.readText() }
}
