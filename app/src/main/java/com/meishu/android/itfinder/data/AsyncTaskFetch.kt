package com.meishu.android.itfinder.data

import android.os.AsyncTask
import com.meishu.android.itfinder.model.Post
import com.meishu.android.itfinder.provider.ItEventsComProvider
import com.meishu.android.itfinder.provider.TimePadProvider

class AsyncTaskFetch(private val query : String?) : AsyncTask<Unit, Unit, List<Post>>() {

    private var listener : DataPreparedListener? = null

    override fun doInBackground(vararg p0: Unit): List<Post> {
        val provider = ItEventsComProvider()

        val timepad = TimePadProvider()
        val result = ArrayList<Post>()

        when (query) {
            null -> result.addAll(timepad.fetchPosts())
            else -> result.addAll(timepad.searchPosts(query))
        }

        result.addAll(provider.provide())
        return result
    }

    override fun onPostExecute(result: List<Post>) {
        listener?.retrieveNewData(result)
    }

    fun setListener(listener : DataPreparedListener?) {
        this.listener = listener
    }

}