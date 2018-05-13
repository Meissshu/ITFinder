package com.meishu.android.itfinder.provider

import com.meishu.android.itfinder.model.Post

interface Provider {

    fun fetchPosts(): List<Post>
    fun searchPosts(query: String): List<Post>
}