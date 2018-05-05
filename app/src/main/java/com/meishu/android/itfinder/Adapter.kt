package com.meishu.android.itfinder

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.meishu.android.itfinder.model.Post
import kotlinx.android.synthetic.main.post_template.view.*

/**
 * Created by Meishu on 18.02.2018.
 */
class Adapter(private val posts : List<Post>) : RecyclerView.Adapter<Adapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.post_template, parent, false)
    )

    override fun getItemCount(): Int = posts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]
        holder.bind(post)
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        private val title : TextView = itemView.post_title
        private val time : TextView = itemView.post_time
        private val image : ImageView = itemView.post_image

        fun bind(item : Post) = with(itemView) {
            title.text = item.title
            time.text = item.time.toString()
            image.setImageResource(R.drawable.bfa8bb19)
            //image.loadUrl(item.imageUrl)
        }
    }
}

private fun ImageView.loadUrl(imageUrl: String) {
    // todo: implement
}
