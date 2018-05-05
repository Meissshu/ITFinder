package com.meishu.android.itfinder.model

/**
 * Created by Meishu on 18.02.2018.
 */
data class Post(var title : String = "", var time : Long = 0, var source : String = "Inner source", var imageUrl: String = "", var isLiked : Boolean = false, var place : String = "", var href : String = "") {


    override fun toString(): String {
        return "Post(title='$title', time=$time, source='$source', imageUrl='$imageUrl', isLiked=$isLiked, place='$place', href='$href')"
    }
}