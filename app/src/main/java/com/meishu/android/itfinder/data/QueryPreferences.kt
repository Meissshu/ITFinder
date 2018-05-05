package com.meishu.android.itfinder.data

import android.content.Context
import android.preference.PreferenceManager

object QueryPreferences {
    private const val PREF_SEARCH_QUERY = "searchQuery"

    fun getStoredQuery(context: Context): String? =
            PreferenceManager
                    .getDefaultSharedPreferences(context)
                    .getString(PREF_SEARCH_QUERY, null)

    fun setStoredQuery(context: Context, query: String) =
            PreferenceManager
                    .getDefaultSharedPreferences(context)
                    .edit()
                    .putString(PREF_SEARCH_QUERY, query)
                    .apply()
}
