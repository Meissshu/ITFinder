package com.meishu.android.itfinder.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.preference.PreferenceFragmentCompat
import android.support.v7.preference.PreferenceScreen
import com.meishu.android.itfinder.R

/**
 * Created by Meishu on 25.02.2018.
 */
class SettingsFragment : PreferenceFragmentCompat(),
        SharedPreferences.OnSharedPreferenceChangeListener,
        PreferenceFragmentCompat.OnPreferenceStartScreenCallback/*,
        Preference.OnPreferenceChangeListener */{

    override fun onPreferenceStartScreen(caller: PreferenceFragmentCompat, pref: PreferenceScreen): Boolean {
        caller.preferenceScreen = pref
        return true
    }

    override fun getCallbackFragment(): Fragment {
        return this
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)

//        val preferenceScreen = preferenceScreen
//        val sharedPreferences = preferenceScreen.sharedPreferences

    }

    override fun onSharedPreferenceChanged(sharedPreference: SharedPreferences?, key: String?) {
        val preference = findPreference(key) ?: return
        println("PREFERENCE HAS CHANGED ${preference.key}")
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }
}