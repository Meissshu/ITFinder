package com.meishu.android.itfinder.data

import android.content.SharedPreferences
import android.content.res.Resources
import com.meishu.android.itfinder.DB.PostRepository
import com.meishu.android.itfinder.R
import com.meishu.android.itfinder.fragments.LikedFragment
import com.meishu.android.itfinder.model.Post
import com.meishu.android.itfinder.provider.ItEventsComProvider
import com.meishu.android.itfinder.provider.Provider
import com.meishu.android.itfinder.provider.TimePadProvider

class DataCenter(resources: Resources, sharedPrefs: SharedPreferences) {

    companion object {
        const val SOURCE_KEY = "dataSourceListPref"
        private val sources = ArrayList<Provider>()

        fun addSource(provider: Provider) {
            sources.add(provider)
        }

        fun removeSource(provider: Provider) {
            sources.remove(provider)
        }

        fun provideData(query: String?): List<Post> {
            val data = ArrayList<Post>()

            when (query) {
                null -> {
                    for (provider in sources) {
                        data.addAll(provider.fetchPosts())
                    }
                }

                LikedFragment.LIKED_QUERY -> {
                    data.addAll(PostRepository.getAll())
                }

                "" -> {
                    val pass = Unit
                }

                else -> {
                    for (provider in sources) {
                        data.addAll(provider.searchPosts(query))
                    }
                }
            }

            return sortData(data)
        }

        private fun sortData(data: List<Post>): List<Post> = data.sortedByDescending { post -> post.time }
    }

    init {
        val allSources = mapOf(
                resources.getString(R.string.it_events) to ItEventsComProvider(),
                resources.getString(R.string.timepad) to TimePadProvider()
        )

        val selections = sharedPrefs.getStringSet(SOURCE_KEY, null)
        for (name in selections) {
            sources.add(allSources[name]!!)
        }
    }

}