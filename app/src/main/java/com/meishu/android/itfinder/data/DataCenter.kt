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

class DataCenter(sharedPrefs: SharedPreferences, resources: Resources) {

    companion object {
        var DEFAULT_THRESHOLD = 10
        lateinit var SOURCE_KEY: String
        lateinit var THRESHOLD_KEY: String
        private val sources = ArrayList<Provider>()
        private lateinit var allSources: Map<String, Provider>

        fun updateWithNewSet(preferences: SharedPreferences) {
            sources.clear()
            val selections = preferences.getStringSet(SOURCE_KEY, null)
            for (name in selections) {
                sources.add(allSources[name]!!)
            }
        }

        fun provideData(query: String?): List<Post> {
            val data = ArrayList<Post>()

            when (query) {
                null -> {
                    for (provider in sources) {
                        data.addAll(provider.fetchPosts(DEFAULT_THRESHOLD))
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
                        data.addAll(provider.searchPosts(query, DEFAULT_THRESHOLD))
                    }
                }
            }

            return sortData(data)
        }

        private fun sortData(data: List<Post>): List<Post> = data.sortedByDescending { post -> post.time }
    }

    init {
        allSources = mapOf(
                resources.getString(R.string.it_events) to ItEventsComProvider(),
                resources.getString(R.string.timepad) to TimePadProvider()
        )
        SOURCE_KEY = resources.getString(R.string.data_source_key)
        THRESHOLD_KEY = resources.getString(R.string.seek_bar_key)
        updateWithNewSet(sharedPrefs)
    }
}