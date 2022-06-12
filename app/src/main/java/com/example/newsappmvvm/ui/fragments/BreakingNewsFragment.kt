package com.example.newsappmvvm.ui.fragments

import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsappmvvm.R
import com.example.newsappmvvm.adapters.NewsAdapter
import com.example.newsappmvvm.ui.NewsViewModel
import com.example.newsappmvvm.ui.fragments.activities.NewsActivity
import com.example.newsappmvvm.util.Resource
import retrofit2.Response

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news){

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter : NewsAdapter

    lateinit var rvBreakingNews: RecyclerView
    lateinit var progressbar: ProgressBar

    val TAG = "BreakingNewsFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // finding their id's
        rvBreakingNews = view.findViewById(R.id.rvBreakingNews)
        progressbar = view.findViewById(R.id.paginationProgressBar)

        viewModel = (activity as NewsActivity).viewModel

        setupRecyclerView()

        // added naviagation to article fragment
        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment2_to_articleFragment,
                bundle
            )
        }

        viewModel.breakingNewsList.observe(viewLifecycleOwner, Observer { responce ->
            when(responce){

                // if success
                is Resource.Success -> {
                    hideProgressBar()

                    responce.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }

                // is error
                is Resource.Error -> {
                    hideProgressBar()

                    responce.message?.let { message ->
                        Log.e(TAG, "An error occured $message")
                    }
                }

                // if not load
                is Resource.Loading -> {
                    showProgressBar()
                }
            }

        })


    }

    private fun hideProgressBar(){
        progressbar.visibility = View.INVISIBLE
    }

    private fun showProgressBar(){
        progressbar.visibility = View.VISIBLE
    }

    private fun setupRecyclerView(){
        newsAdapter = NewsAdapter()
        rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}