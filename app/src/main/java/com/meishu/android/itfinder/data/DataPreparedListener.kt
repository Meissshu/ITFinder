package com.meishu.android.itfinder.data

import com.meishu.android.itfinder.model.Post

interface DataPreparedListener {
        fun retrieveNewData(data : List<Post>)
}