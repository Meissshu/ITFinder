package com.meishu.android.itfinder.fragments

import android.os.Bundle
import android.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.meishu.android.itfinder.R
import com.meishu.android.itfinder.Adapter
import com.meishu.android.itfinder.model.Post

/**
 * Created by Meishu on 18.02.2018.
 */
class LikedFragment : Fragment() {

    val data : List<Post> = List(5) {Post("Title $it", it.toLong(), "Description $it", it.toString())}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.liked_fragment, null)
        val recycle = rootView.findViewById<RecyclerView>(R.id.recycler_view_liked)
        val layoutManager = LinearLayoutManager(activity)
        val adapter = Adapter(data)

        recycle.layoutManager = layoutManager
        recycle.adapter = adapter

        return rootView
    }

}