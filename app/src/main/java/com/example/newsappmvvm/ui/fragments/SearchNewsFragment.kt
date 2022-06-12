package com.example.newsappmvvm.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsappmvvm.R
import com.example.newsappmvvm.adapters.NewsAdapter
import com.example.newsappmvvm.ui.NewsViewModel
import com.example.newsappmvvm.ui.fragments.activities.NewsActivity
import com.example.newsappmvvm.util.Constants.Companion.Search_News_Time_Delay
import com.example.newsappmvvm.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : Fragment(R.layout.fragment_search_news){

    lateinit var viewModel: NewsViewModel
    lateinit var searchNewsAdapter: NewsAdapter

    lateinit var rvBreakingNews: RecyclerView
    lateinit var progressbar: ProgressBar
    lateinit var etSearch: EditText

    val TAG = "SearchNewsFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // finding their id's
        rvBreakingNews = view.findViewById(R.id.rvSearchNews)
        progressbar = view.findViewById(R.id.searchProgressBar)
        etSearch = view.findViewById(R.id.etSearch)

        viewModel = (activity as NewsActivity).viewModel

        setupRecyclerView()

        // search krne par delay dikhane ke liye
        var job : Job? = null
        etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(Search_News_Time_Delay)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.searchNewsList(editable.toString())
                    }

                }
            }
        }

        // added naviagation to article fragment
        searchNewsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_searchNewsFragment_to_articleFragment,
                bundle
            )
        }

        viewModel.searchNewsList.observe(viewLifecycleOwner, Observer { responce ->

            when(responce){

                // if success
                is Resource.Success -> {
                    hideProgressBar()

                    responce.data?.let { newsResponse ->
                        searchNewsAdapter.differ.submitList(newsResponse.articles)
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
        searchNewsAdapter = NewsAdapter()
        rvBreakingNews.apply {
            adapter = searchNewsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
    }
