package com.dicoding.ecosorter.view.article

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.dicoding.ecosorter.R
import com.dicoding.ecosorter.repository.Article

class DetailArticleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_article)

        val article = intent.getParcelableExtra<Article>("ARTICLE")
        if (article != null) {
            displayArticle(article)
        } else {
            Toast.makeText(this, "Article data not found.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun displayArticle(article: Article) {
        val articleImageView: ImageView = findViewById(R.id.articleImageView)
        val articleTitleTextView: TextView = findViewById(R.id.articleTitleTextView)
        val articleDescriptionTextView: TextView = findViewById(R.id.articleDescriptionTextView)

        Glide.with(this)
            .load(article.photo)
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.ic_baseline_image_not_supported_24)
            .into(articleImageView)
        articleTitleTextView.text = article.title
        articleDescriptionTextView.text = article.description
    }
}