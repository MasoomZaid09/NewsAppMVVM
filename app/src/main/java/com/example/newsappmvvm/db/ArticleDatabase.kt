package com.example.newsappmvvm.db

import android.content.Context
import androidx.room.*
import com.example.newsappmvvm.models.Article
import java.util.concurrent.locks.Lock

@Database(
    entities = [Article::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase(){

    abstract fun getArticleDao() : ArticleDao

    companion object{

        @Volatile
        private var instance : ArticleDatabase? = null
        private var Lock = Any()

        operator fun invoke(context : Context) = instance ?: synchronized(Lock){
            instance ?: createDatabase(context).also{
                instance = it
            }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ArticleDatabase::class.java,
                "articleDB.db"
            ).build()

    }

}