package com.example.newsappmvvm.ui

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewModelScope
import com.example.newsappmvvm.models.Article
import com.example.newsappmvvm.models.NewsResponse
import com.example.newsappmvvm.repository.NewsRepository
import com.example.newsappmvvm.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
    val newsRepository: NewsRepository
) : ViewModel(){

    // live data ka object bnaya
    val breakingNewsList: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1

    val searchNewsList: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1

    init {
        getBreakingNews("en")
    }

    fun getBreakingNews(countryCode : String) = viewModelScope.launch {
        breakingNewsList.postValue(Resource.Loading())
        val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
        breakingNewsList.postValue(handleBreakingNewsResponce(response))
    }

    fun getSearchNews(searchQuery : String) = viewModelScope.launch {
        searchNewsList.postValue(Resource.Loading())
        val response = newsRepository.getSearchNews(searchQuery, searchNewsPage)
        searchNewsList.postValue(handleSearchNewsResponce(response))
    }

    private fun handleBreakingNewsResponce(response: Response<NewsResponse>) : Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let {resultResponce ->
                return Resource.Success(resultResponce)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponce(response: Response<NewsResponse>) : Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let {resultResponce ->
                return Resource.Success(resultResponce)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.delete(article)
    }

    fun getSavedNews() = newsRepository.getSavedNews()


}