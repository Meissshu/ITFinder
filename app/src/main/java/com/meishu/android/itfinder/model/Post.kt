package com.meishu.android.itfinder.model

import com.meishu.android.itfinder.DB.AppDatabase
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table


/**
 * Created by Meishu on 18.02.2018.
 */
@Table(name = "posts", database = AppDatabase::class)
data class Post(
        @PrimaryKey(autoincrement = true)
        var id: Int = 0,
        @Column
        var title: String = "",
        @Column
        var time: Long = 0,
        @Column
        var source: String = "Inner source",
        @Column
        var imageUrl: String = "",
        var isLiked: Boolean = false,
        @Column
        var place: String = "",
        @Column
        var href: String = "") {

    override fun toString(): String {
        return "Post(title='$title', time=$time, source='$source', imageUrl='$imageUrl', isLiked=$isLiked, place='$place', href='$href')"
    }
}