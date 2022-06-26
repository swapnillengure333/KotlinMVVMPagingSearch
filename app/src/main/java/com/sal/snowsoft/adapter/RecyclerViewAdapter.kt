package com.sal.snowsoft.adapter

import android.graphics.Color
import android.net.Uri
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sal.snowsoft.R
import com.sal.snowsoft.model.ContentData
import java.io.FileNotFoundException
import java.util.logging.Logger


class RecyclerViewAdapter() :
    PagingDataAdapter<ContentData, RecyclerViewAdapter.MyViewHolder>(DiffUtilCallBack()) {
    var query = "";
    override fun onBindViewHolder(holder: RecyclerViewAdapter.MyViewHolder, position: Int) {

        holder.bind(getItem(position)!!,query.lowercase())
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewAdapter.MyViewHolder {
        val inflater =
            LayoutInflater.from(parent.context).inflate(R.layout.item_grid, parent, false)

        return MyViewHolder(inflater)
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val imageView: ImageView = view.findViewById(R.id.imageView)
        val tvName: TextView = view.findViewById(R.id.tvName)

        fun bind(data: ContentData, query: String) {
            if(query.isEmpty()){
                tvName.text = data.name
            }else{
                //adding highlighter in filter character
                var title = data.name.lowercase()
                var start = title.indexOf(query)
                if(start!=-1){
                    val wordtoSpan: Spannable =
                        SpannableString(data.name)
                    wordtoSpan.setSpan(
                        ForegroundColorSpan(Color.YELLOW),
                        start,
                        start+query.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    tvName.text = wordtoSpan
                }else{
                    tvName.text = data.name
                }
            }
            try {
                Glide.with(imageView)
                    .load(
                        Uri.parse("file:///android_asset/${data.poster_image}")
                    )
                    .placeholder(R.drawable.ic_placeholder)
                    .into(imageView)
            }catch (e:FileNotFoundException){
                e.stackTrace
            }
        }
    }

    class DiffUtilCallBack : DiffUtil.ItemCallback<ContentData>() {
        override fun areItemsTheSame(oldItem: ContentData, newItem: ContentData): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: ContentData, newItem: ContentData): Boolean {
            return oldItem.name == newItem.name
                    && oldItem.poster_image == newItem.poster_image
        }

    }
    fun setSearchQuery(query:String){
        this.query = query
    }
}