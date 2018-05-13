package com.meishu.android.itfinder.DB

import com.raizlabs.android.dbflow.annotation.Database

@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
class AppDatabase {
    companion object {
        const val NAME = "FinderDb"
        const val VERSION = 1
    }
}