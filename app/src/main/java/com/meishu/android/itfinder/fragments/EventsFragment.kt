package com.meishu.android.itfinder.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.meishu.android.itfinder.R
import com.meishu.android.itfinder.data.AsyncTaskFetch
import com.meishu.android.itfinder.data.DataPreparedListener
import com.meishu.android.itfinder.model.Post

/**
 * Created by Meishu on 18.02.2018.
 */
class EventsFragment : BaseFragment() {
    override fun provideEmptyTextTag(): Int = R.id.events_empty_data

    override fun provideRecyclerTag(): Int = R.id.recycler_view_events

    override fun provideLayout(): Int = R.layout.events_fragment

    private lateinit var progressBar: ImageView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView : View = super.onCreateView(inflater, container, savedInstanceState)!!

        progressBar = rootView.findViewById(R.id.events_progress_bar)
        updateItems(null, progressBar)

        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        removeAsyncListener()
    }
}