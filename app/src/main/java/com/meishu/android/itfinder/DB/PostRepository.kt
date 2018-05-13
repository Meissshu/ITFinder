package com.meishu.android.itfinder.DB

import com.meishu.android.itfinder.model.Post
import com.meishu.android.itfinder.model.Post_Table
import com.raizlabs.android.dbflow.sql.language.Select

object PostRepository {

    fun getAll(): List<Post> {
        return Select()
                .from(Post::class.java)
                .where()
                .orderBy(Post_Table.time, false)
                .queryList()
    }
}