package com.meishu.android.itfinder.data

import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.meishu.android.itfinder.R
import com.meishu.android.itfinder.fragments.EventsFragment
import com.meishu.android.itfinder.model.Post
import java.text.SimpleDateFormat
import java.util.*

class PostAdapter(
        private val posts : List<Post>,
        private val downloader: ThumbnailDownloader<PostHolder>,
        val context: Context
) : RecyclerView.Adapter<PostAdapter.PostHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.post_template, parent, false)
        return PostHolder(view)
    }

    override fun getItemCount() = posts.size

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        val post = posts[position]
        holder.bindPost(post)
        downloader.queueThumbnail(holder, post.imageUrl)
    }

    inner class PostHolder(item : View) : RecyclerView.ViewHolder(item), View.OnClickListener {

        private val title : TextView = item.findViewById(R.id.post_title)
        private val time : TextView = item.findViewById(R.id.post_time)
        private val source : TextView = item.findViewById(R.id.post_source_text)
        private val image : ImageView = item.findViewById(R.id.post_image)
        private val place : TextView = item.findViewById(R.id.post_place)
        private var url : String = ""

        fun bindPost(post : Post) {
            title.text = post.title
            time.text = longToDate(post.time)
            source.text = post.source
            place.text = post.place
            url = post.href
            image.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            context.startActivity(i)
        }

        fun bindDrawable(drawable: BitmapDrawable) {
            image.setImageDrawable(drawable)
        }

        private fun longToDate(time : Long) : String {
            val date = Date(time)
            val df = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
            return df.format(date)
        }
    }
}