package com.meishu.android.itfinder.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceScreen
import com.meishu.android.itfinder.R

/**
 * Created by Meishu on 25.02.2018.
 */
class SettingsFragment : PreferenceFragmentCompat(),
        SharedPreferences.OnSharedPreferenceChangeListener/*,
        Preference.OnPreferenceChangeListener */{

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)

//        val preferenceScreen = preferenceScreen
//        val sharedPreferences = preferenceScreen.sharedPreferences

    }

    override fun onSharedPreferenceChanged(sharedPreference: SharedPreferences?, key: String?) {
        val preference = findPreference(key) ?: return
        println("PREFERENCE HAS CHANGED ${preference.key}")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }
}