package com.example.newsappmvvm.repository

import com.example.newsappmvvm.api.RetrofitInstance
import com.example.newsappmvvm.db.ArticleDatabase
import com.example.newsappmvvm.models.Article

class NewsRepository(
    val db : ArticleDatabase
) {

    // api se responce le liya breaking news ka
    suspend fun getBreakingNews(countryCode : String, pageNumber : Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    // api se responce le liya search news ka
    suspend fun getSearchNews(searchQuery : String, pageNumber : Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

    // upsert , delete , getAllArticles
    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun delete(article: Article) = db.getArticleDao().deleteArticle(article)


}