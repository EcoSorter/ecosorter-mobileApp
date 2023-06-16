package com.dicoding.ecosorter.view.popUp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.dicoding.ecosorter.R

class ImageStatusActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_status)

        // Get the response result from the intent
        val responseResult = intent.getStringExtra("imageStatus")

        // Display the article based on the response result
        val articleTitle = findViewById<TextView>(R.id.articleTitle)
        val articleContent = findViewById<TextView>(R.id.articleContent)

        when (responseResult) {
            "B" -> {
                articleTitle.text = getString(R.string.Biodegradable)
                articleContent.text = getString(R.string.bioContent)
            }
            "N" -> {
                articleTitle.text = getString(R.string.NonBiodegradable)
                articleContent.text = getString(R.string.NonBioContent)
            }
        }
    }
}