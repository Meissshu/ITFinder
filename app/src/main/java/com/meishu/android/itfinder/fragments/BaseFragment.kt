package com.meishu.android.itfinder.fragments

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.meishu.android.itfinder.data.PostAdapter
import com.meishu.android.itfinder.model.Post

/**
 * Created by Meishu on 18.02.2018.
 */
abstract class BaseFragment : Fragment() {

    abstract fun provideLayout() : Int
    abstract fun provideTag() : String
    abstract fun provideRecyclerTag() : Int

    var data : List<Post> = ArrayList()
    private lateinit var recycle : RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        // setup()
        Log.i(provideTag(), "Background thread started")
    }

    /*
    fun setup() {
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
    }
     */

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(provideLayout(), null)
        recycle = rootView.findViewById(provideRecyclerTag())
        val layoutManager = LinearLayoutManager(this.activity)
        val scrollPosition = (recycle.layoutManager as? LinearLayoutManager)?.findFirstCompletelyVisibleItemPosition() ?: 0

        recycle.layoutManager = layoutManager
        recycle.scrollToPosition(scrollPosition)
        setupAdapter()

        return rootView
    }

    fun setupAdapter() {
        if (isAdded) {
            val adapter = PostAdapter(data, activity)
            recycle.adapter = adapter
        }
    }
}