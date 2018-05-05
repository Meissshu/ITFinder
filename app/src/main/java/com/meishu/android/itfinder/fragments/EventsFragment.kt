package com.meishu.android.itfinder.fragments

import android.os.Bundle
import com.meishu.android.itfinder.R
import com.meishu.android.itfinder.data.AsyncTaskFetch
import com.meishu.android.itfinder.data.DataPreparedListener
import com.meishu.android.itfinder.model.Post

/**
 * Created by Meishu on 18.02.2018.
 */
class EventsFragment : BaseFragment() {
    override fun provideRecyclerTag(): Int = R.id.recycler_view_events

    override fun provideTag(): String = "Events fragment"

    override fun provideLayout(): Int = R.layout.events_fragment

    private lateinit var asyncTask: AsyncTaskFetch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        asyncTask = AsyncTaskFetch(null)
        asyncTask.setListener(object : DataPreparedListener {
            override fun retrieveNewData(data: List<Post>) {
                this@EventsFragment.data = data
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