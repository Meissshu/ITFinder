package com.meishu.android.itfinder


import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.meishu.android.itfinder.fragments.EventsFragment
import com.meishu.android.itfinder.fragments.LikedFragment
import com.meishu.android.itfinder.fragments.SearchFragment

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true) // ???

        viewPager = findViewById(R.id.viewpager)
        setupViewPager(viewPager)

        tabLayout = findViewById(R.id.tabs)
        tabLayout.setupWithViewPager(viewPager) // autorefresh?


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
            when (item.itemId) {
                R.id.menu_about -> {
                    Log.i("tag", "menu about clicked")
                    true
                }

                R.id.menu_bug_report -> {
                    Log.i("tag", "menu bug report")
                    true
                }

                R.id.menu_settings -> {
                    Log.i("tag", "menu settings")
                    true
                }

                else -> super.onOptionsItemSelected(item)
            }


    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(EventsFragment(), "Events")
        adapter.addFragment(SearchFragment(), "Search")
        adapter.addFragment(LikedFragment(), "Liked")
        viewPager.adapter = adapter
    }

    class ViewPagerAdapter(manager : FragmentManager) : FragmentPagerAdapter(manager) {

        private val fragmentList = ArrayList<Fragment>()
        private val fragmentTitleList = ArrayList<String>()

        override fun getItem(position: Int): Fragment = fragmentList[position]

        override fun getCount(): Int = fragmentList.size

        override fun getPageTitle(position: Int): CharSequence = fragmentTitleList[position]

        fun addFragment(fragment: Fragment, title : String) {
            fragmentList.add(fragment)
            fragmentTitleList.add(title)
        }
    }
}
