package com.meishu.android.itfinder.model

/**
 * Created by Meishu on 18.02.2018.
 */
data class Post(var title : String = "", var time : Long = 0, var description : String = "", var imageUrl: String = "", var tags : Array<String> = emptyArray(), var isLiked : Boolean = false, var place : String = "", var href : String = "") {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Post

        if (title != other.title) return false
        if (time != other.time) return false
        if (description != other.description) return false
        if (imageUrl != other.imageUrl) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + time.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + imageUrl.hashCode()
        return result
    }
}