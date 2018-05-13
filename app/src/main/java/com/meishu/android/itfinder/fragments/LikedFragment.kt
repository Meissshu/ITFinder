package com.meishu.android.itfinder.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.meishu.android.itfinder.R

/**
 * Created by Meishu on 18.02.2018.
 */
class LikedFragment : BaseFragment() {

    companion object {
        const val LIKED_QUERY = "db_fetch_liked_post_query"
    }

    override fun provideEmptyTextTag(): Int = R.id.liked_empty_data

    override fun provideLayout(): Int = R.layout.liked_fragment

    override fun provideRecyclerTag(): Int = R.id.recycler_view_liked

    private lateinit var progressBar: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View = super.onCreateView(inflater, container, savedInstanceState)!!

        progressBar = rootView.findViewById(R.id.liked_progress_bar)
        refresh()

        return rootView
    }

    fun refresh() {
        updateItems(LIKED_QUERY, progressBar)
    }

}