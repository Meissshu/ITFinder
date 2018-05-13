package com.meishu.android.itfinder.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import com.meishu.android.itfinder.R
import com.meishu.android.itfinder.data.QueryPreferences


/**
 * Created by Meishu on 18.02.2018.
 */
class TrackedFragment : BaseFragment() {

    companion object {
        fun newIntent(context: Context) = Intent(context, TrackedFragment::class.java)
    }

    override fun provideEmptyTextTag(): Int = R.id.tracked_empty_data

    override fun provideRecyclerTag(): Int = R.id.recycler_view_tracked

    override fun provideLayout(): Int = R.layout.tracked_fragment


    private lateinit var trackedItem: TextInputEditText
    private lateinit var progressBar: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = super.onCreateView(inflater, container, savedInstanceState)!!

        progressBar = view.findViewById(R.id.tracked_progress_bar)
        updateItems(QueryPreferences.getStoredQuery(activity, QueryPreferences.PREF_TRACKED_QUERY), progressBar)

        trackedItem = view.findViewById(R.id.tracked_text_input)
        trackedItem.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                QueryPreferences.setStoredQuery(activity, QueryPreferences.PREF_TRACKED_QUERY, trackedItem.text.toString())
                updateItems(QueryPreferences.getStoredQuery(activity, QueryPreferences.PREF_TRACKED_QUERY), progressBar)
                closeKeyboard()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        trackedItem.setText(QueryPreferences.getStoredQuery(activity, QueryPreferences.PREF_TRACKED_QUERY))

        TrackService.setServiceAlarm(activity, true)
        return view
    }

    private fun closeKeyboard() {
        val view = activity.currentFocus

        if (view != null) {
            val imm = activity.applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(this.view.rootView.windowToken, 0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        removeAsyncListener()
    }

}