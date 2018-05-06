package com.meishu.android.itfinder.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import com.meishu.android.itfinder.R
import com.meishu.android.itfinder.data.AsyncTaskFetch
import com.meishu.android.itfinder.data.DataPreparedListener
import com.meishu.android.itfinder.data.QueryPreferences
import com.meishu.android.itfinder.model.Post


/**
 * Created by Meishu on 18.02.2018.
 */
class SearchFragment : BaseFragment() {
    override fun provideEmptyTextTag(): Int = R.id.search_empty_data

    override fun provideRecyclerTag(): Int = R.id.recycler_view_search

    override fun provideTag(): String = "SearchFragment"

    override fun provideLayout(): Int = R.layout.search_fragment

    private lateinit var asyncTask: AsyncTaskFetch
    private lateinit var searchItem : SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateItems()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = super.onCreateView(inflater, container, savedInstanceState)!!
        searchItem = view.findViewById(R.id.search_view)
        searchItem.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Log.d(provideTag(), "QueryTextSubmit: $query")
                QueryPreferences.setStoredQuery(activity, query)
                updateItems()
                closeKeyboard()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                Log.d(provideTag(), "QueryTextChange: $newText")
                return false
            }

        })

        searchItem.setOnSearchClickListener { searchItem.setQuery(QueryPreferences.getStoredQuery(activity), false) }

        return view
    }

    private fun closeKeyboard() {
        val view = activity.currentFocus

        if (view != null) {
            val imm = activity.applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(this.view.rootView.windowToken, 0)
        }
    }

    private fun updateItems() {
        asyncTask = AsyncTaskFetch(QueryPreferences.getStoredQuery(activity))
        asyncTask.setListener(object : DataPreparedListener {
            override fun retrieveNewData(data: List<Post>) {
                this@SearchFragment.data = data
                setupAdapter()
            }
        })
        asyncTask.execute()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        asyncTask.setListener(null)
    }

}