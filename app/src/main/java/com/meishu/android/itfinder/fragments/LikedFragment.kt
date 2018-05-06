package com.meishu.android.itfinder.fragments

import com.meishu.android.itfinder.R

/**
 * Created by Meishu on 18.02.2018.
 */
class LikedFragment : BaseFragment() {
    override fun provideEmptyTextTag(): Int = R.id.liked_empty_data

    override fun provideLayout(): Int = R.layout.liked_fragment
    override fun provideTag(): String = "LikedFragment"

    override fun provideRecyclerTag(): Int = R.id.recycler_view_liked


}