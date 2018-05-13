package com.meishu.android.itfinder.data

import android.content.Context
import android.preference.PreferenceManager

object QueryPreferences {
    const val PREF_SEARCH_QUERY = "searchQuery"
    const val PREF_TRACKED_QUERY = "trackedQuery"
    const val PREF_LAST_RESULT_IT = "lastResultId"

    fun getStoredQuery(context: Context, type: String): String? =
            PreferenceManager
                    .getDefaultSharedPreferences(context)
                    .getString(type, null)

    fun setStoredQuery(context: Context, type: String, query: String) =
            PreferenceManager
                    .getDefaultSharedPreferences(context)
                    .edit()
                    .putString(type, query)
                    .apply()
}

