package com.dicoding.ecosorter.view.article

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.dicoding.ecosorter.R
import com.dicoding.ecosorter.repository.Article

class DetailArticleFragment : Fragment() {

    private lateinit var article: Article

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            article = it.getParcelable(ARG_ARTICLE)!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_detail_article, container, false)
        val titleTextView: TextView = view.findViewById(R.id.articleTitleTextView)
        titleTextView.text = article.title
        val descriptionTextView: TextView = view.findViewById(R.id.articleDescriptionTextView)
        descriptionTextView.text = article.description
        val imageView: ImageView = view.findViewById(R.id.articleImageView)
        Glide.with(view.context).load(article.photo).into(imageView)
        return view
    }

    companion object {
        private const val ARG_ARTICLE = "article"

        fun newInstance(article: Article): DetailArticleFragment {
            val fragment = DetailArticleFragment()
            val args = Bundle()
            args.putParcelable(ARG_ARTICLE, article)
            fragment.arguments = args
            return fragment
        }
    }
}