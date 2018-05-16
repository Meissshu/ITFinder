package com.meishu.android.itfinder.provider

import com.meishu.android.itfinder.model.Post

interface Provider {

    fun fetchPosts(threshold: Int): List<Post>
    fun searchPosts(query: String, threshold: Int): List<Post>
}