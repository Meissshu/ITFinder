package com.meishu.android.itfinder.fragments

import android.app.Fragment
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.meishu.android.itfinder.R
import com.meishu.android.itfinder.data.ThumbnailDownloader
import com.meishu.android.itfinder.data.ThumbnailDownloader.ThumbnailDownloadListener
import com.meishu.android.itfinder.model.Post
import kotlin.collections.ArrayList
import com.meishu.android.itfinder.data.AsyncTaskFetch
import com.meishu.android.itfinder.data.PostAdapter

/**
 * Created by Meishu on 18.02.2018.
 */
abstract class BaseFragment : Fragment() {

    abstract fun provideLayout() : Int
    abstract fun provideTag() : String

    var data : List<Post> = ArrayList()
    private lateinit var recycle : RecyclerView
    private lateinit var downloader : ThumbnailDownloader<PostAdapter.PostHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

        val responseHandler = Handler()
        downloader = ThumbnailDownloader(responseHandler)
        downloader.thumbnailListener = object : ThumbnailDownloadListener<PostAdapter.PostHolder> {
            override fun onThumbnailDownloaded(target: PostAdapter.PostHolder, thumbnail: Bitmap) {
                val drawable = BitmapDrawable(resources, thumbnail)
                target.bindDrawable(drawable)
            }
        }

        downloader.start()
        downloader.looper
        Log.i(provideTag(), "Background thread started")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(provideLayout(), null)
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

    override fun onDestroy() {
        super.onDestroy()
        downloader.quit()
        Log.i(provideTag(), "Background thread stopped")
    }

    fun setupAdapter() {
        if (isAdded) {
            val adapter = PostAdapter(data, downloader, activity)
            recycle.adapter = adapter
        }
    }
}