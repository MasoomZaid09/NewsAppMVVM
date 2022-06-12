package com.example.newsappmvvm.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsappmvvm.R
import com.example.newsappmvvm.db.ArticleDatabase
import com.example.newsappmvvm.models.Article

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val ivArtivleImage : ImageView = itemView.findViewById(R.id.ivArticleImage)
        val tvSource : TextView = itemView.findViewById(R.id.tvSource)
        val tvTitle : TextView = itemView.findViewById(R.id.tvTitle)
        val tvDescription : TextView = itemView.findViewById(R.id.tvDescription)
        val tvPublishedAt : TextView = itemView.findViewById(R.id.tvPublishedAt)
    }

    // user Differ utils for change only those data which is change if set dataSetNotifyChanged()
    // then is will change every time even if the data is not change so thats why we use diff utils method
    private val differCallback = object  : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    // it is Async so it will run in diffrent thread
    val differ = AsyncListDiffer(this, differCallback)

    // normal recycler view code
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_row_article_items, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {

        val article = differ.currentList[position]

        holder.itemView.apply {
            Glide.with(this).load(article.urlToImage).into(holder.ivArtivleImage)
            holder.tvSource.text = article.source.name
            holder.tvTitle.text = article.title
            holder.tvDescription.text = article.description
            holder.tvPublishedAt.text = article.publishedAt

            setOnClickListener {
                onItemClickListener?.let {
                    it(article)
                }
            }
        }
    }

    override fun getItemCount(): Int{
        return differ.currentList.size
    }

    // added click on single items of recycler
    private var onItemClickListener : ((Article) -> Unit)? = null

    fun setOnItemClickListener(listener : (Article) -> Unit){
        onItemClickListener = listener
    }
}