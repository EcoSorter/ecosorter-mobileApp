package com.dicoding.ecosorter.view.article

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.ecosorter.R
import com.dicoding.ecosorter.repository.Article


class ArticleAdapter(
    private val articles: List<Article>,
    private val itemClickListener: ArticleFragment
) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.article_card, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles[position]
        holder.bind(article)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.articleTitleTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.articleDescriptionTextView)
        private val imageView: ImageView = itemView.findViewById(R.id.articleImageView)

        fun bind(article: Article) {
            titleTextView.text = article.title
            descriptionTextView.text = article.description
            Glide.with(itemView.context).load(article.photo).into(imageView)

            itemView.setOnClickListener {
                itemClickListener.onArticleItemClicked(article)
            }
        }
    }
}

interface ArticleItemClickListener {
    fun onArticleItemClicked(article: Article)
}