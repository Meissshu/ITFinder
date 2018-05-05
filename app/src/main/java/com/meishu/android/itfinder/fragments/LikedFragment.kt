package com.meishu.android.itfinder.fragments

import android.os.Bundle
import com.meishu.android.itfinder.R
import com.meishu.android.itfinder.data.AsyncTaskFetch
import com.meishu.android.itfinder.data.DataPreparedListener
import com.meishu.android.itfinder.model.Post

/**
 * Created by Meishu on 18.02.2018.
 */
class LikedFragment : BaseFragment() {

    override fun provideTag(): String = "LikedFragment"

    override fun provideLayout(): Int = R.layout.liked_fragment

    private lateinit var asyncTask: AsyncTaskFetch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        asyncTask = AsyncTaskFetch()
        asyncTask.setListener(object : DataPreparedListener {
            override fun retrieveNewData(data: List<Post>) {
                //this@EventsFragment.data = data
                setupAdapter()
            }
        })
        asyncTask.execute()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        asyncTask.setListener(null)
    }
}