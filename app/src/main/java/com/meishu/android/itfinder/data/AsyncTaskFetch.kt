package com.meishu.android.itfinder.data

import android.os.AsyncTask
import com.meishu.android.itfinder.DB.PostRepository
import com.meishu.android.itfinder.fragments.LikedFragment
import com.meishu.android.itfinder.model.Post
import com.meishu.android.itfinder.provider.ItEventsComProvider
import com.meishu.android.itfinder.provider.TimePadProvider

class AsyncTaskFetch(private val query: String?) : AsyncTask<Unit, Unit, List<Post>>() {

    private var listener: DataPreparedListener? = null

    override fun doInBackground(vararg p0: Unit): List<Post> {
        val provider = ItEventsComProvider()
        val timepad = TimePadProvider()

        val result = ArrayList<Post>()

        when (query) {
            null -> {
                result.addAll(timepad.fetchPosts())
                result.addAll(provider.fetchPosts())
            }

            LikedFragment.LIKED_QUERY -> {
                result.addAll(PostRepository.getAll())
            }

            "" -> {
                return result
            }

            else -> {
                result.addAll(timepad.searchPosts(query))
                result.addAll(provider.searchPosts(query))
            }
        }

        result.sortByDescending { post -> post.time }

        return result
    }

    override fun onPostExecute(result: List<Post>) {
        listener?.retrieveNewData(result)
    }

    fun setListener(listener: DataPreparedListener?) {
        this.listener = listener
    }

}