package com.dicoding.ecosorter.view.article

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.ecosorter.R
import com.dicoding.ecosorter.repository.Article

class ArticleFragment : Fragment(), ArticleItemClickListener {

    private lateinit var adapter: ArticleAdapter
    private lateinit var recyclerView: RecyclerView

    private val listArticles: ArrayList<Article>
        get() {
            val dataTitle = resources.getStringArray(R.array.title)
            val dataDescription = resources.getStringArray(R.array.description)
            val dataPhoto = resources.getStringArray(R.array.photo)
            val listArticle = ArrayList<Article>()
            for (i in dataTitle.indices) {
                val article = Article(dataTitle[i], dataDescription[i], dataPhoto[i])
                listArticle.add(article)
            }
            return listArticle
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_article, container, false)
        recyclerView = view.findViewById(R.id.rv_article)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        adapter = ArticleAdapter(listArticles, this)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    override fun onArticleItemClicked(article: Article) {
        val intent = Intent(requireContext(), DetailArticleActivity::class.java)
        intent.putExtra("ARTICLE", article) // Pass the selected article to the DetailArticleActivity
        startActivity(intent)
    }

    companion object {
        fun newInstance(): ArticleFragment {
            return ArticleFragment()
        }
    }
}