package com.jompon.kotlinandroidproject.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.jompon.kotlinandroidproject.fragment.ScreenSlideshowFragment
import com.jompon.kotlinandroidproject.impl.OnItemClickListener
import com.jompon.kotlinandroidproject.model.Gallery

/**
 * Created by Jompon on 12/30/2017.
 */
class ScreenSlidePagerAdapter(fm: FragmentManager, private var galleries: ArrayList<Gallery>) : FragmentStatePagerAdapter(fm) {

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    override fun getItem(position: Int): Fragment {
        return ScreenSlideshowFragment.newInstance(galleries[position])
    }

    override fun getCount(): Int {
        return galleries.size
    }

    fun getItem( ) : ArrayList<Gallery>{
        return galleries
    }
}