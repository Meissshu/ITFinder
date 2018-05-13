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
        return DataCenter.provideData(query)
    }

    override fun onPostExecute(result: List<Post>) {
        listener?.retrieveNewData(result)
    }

    fun setListener(listener: DataPreparedListener?) {
        this.listener = listener
    }

}