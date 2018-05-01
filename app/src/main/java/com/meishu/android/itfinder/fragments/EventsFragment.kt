package com.meishu.android.itfinder.fragments

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.meishu.android.itfinder.R
import com.meishu.android.itfinder.data.ItEventsComProvider
import com.meishu.android.itfinder.data.ThumbnailDownloader
import com.meishu.android.itfinder.data.ThumbnailDownloader.ThumbnailDownloadListener
import com.meishu.android.itfinder.data.TimePadProvider
import com.meishu.android.itfinder.model.Post
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Meishu on 18.02.2018.
 */
class EventsFragment : Fragment() {

    companion object {
        const val TAG : String = "EventsFragment"

        private class FetchItems(val eventsFragment: EventsFragment) : AsyncTask<Unit, Unit, List<Post>>() {

            override fun doInBackground(vararg p0: Unit): List<Post> {
                val provider = ItEventsComProvider()
                val timepad = TimePadProvider()
                val result = ArrayList<Post>()
                result.addAll(timepad.fetchItems())
                result.addAll(provider.provide(TAG))
                return result
            }

            override fun onPostExecute(result: List<Post>) {
                eventsFragment.data = result
                eventsFragment.setupAdapter()
            }
        }
    }

    var data : List<Post> = ArrayList()
    private lateinit var recycle : RecyclerView
    private lateinit var downloader : ThumbnailDownloader<PostHolder>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.events_fragment, null)
        recycle = rootView.findViewById(R.id.recycler_view_events)
        val layoutManager = LinearLayoutManager(this.activity)
        val scrollPosition = (recycle.layoutManager as? LinearLayoutManager)?.findFirstCompletelyVisibleItemPosition() ?: 0

        recycle.layoutManager = layoutManager
        recycle.scrollToPosition(scrollPosition)
        setupAdapter()

        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        downloader.clearQueue()
    }

    fun setupAdapter() {
        if (isAdded) {
            val adapter = PostAdapter(data)
            recycle.adapter = adapter
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        FetchItems(this).execute()

        val responseHandler = Handler()
        downloader = ThumbnailDownloader(responseHandler)
        downloader.thumbnailListener = object : ThumbnailDownloadListener<PostHolder> {
            override fun onThumbnailDownloaded(target: PostHolder, thumbnail: Bitmap) {
                val drawable = BitmapDrawable(resources, thumbnail)
                target.bindDrawable(drawable)
            }
        }


        downloader.start()
        downloader.looper
        Log.i(TAG, "Background thread started")
    }

    override fun onDestroy() {
        super.onDestroy()
        downloader.quit()
        Log.i(TAG, "Background thread stopped")
    }

    private class PostHolder(item : View) : RecyclerView.ViewHolder(item) {

        val title : TextView = item.findViewById(R.id.post_title)
        val time : TextView = item.findViewById(R.id.post_time)
        val description : TextView = item.findViewById(R.id.post_description)
        val image : ImageView = item.findViewById(R.id.post_image)
        val place : TextView = item.findViewById(R.id.post_place)

        fun bindPost(post : Post) {
            title.text = post.title
            time.text = longToDate(post.time)
            description.text = post.description
            place.text = post.place
        }

        fun bindDrawable(drawable: BitmapDrawable) {
            image.setImageDrawable(drawable)
        }

        fun longToDate(time : Long) : String {
            val date = Date(time)
            val df = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
            return df.format(date)
        }
    }

    private inner class PostAdapter(val posts : List<Post>) : RecyclerView.Adapter<PostHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
            val inflater = LayoutInflater.from(activity)
            val view = inflater.inflate(R.layout.post_template, parent, false)
            return PostHolder(view)
        }

        override fun getItemCount() = posts.size

        override fun onBindViewHolder(holder: PostHolder, position: Int) {
            val post = posts[position]
            holder.bindPost(post)
            downloader.queueThumbnail(holder, post.imageUrl)
        }

    }
}