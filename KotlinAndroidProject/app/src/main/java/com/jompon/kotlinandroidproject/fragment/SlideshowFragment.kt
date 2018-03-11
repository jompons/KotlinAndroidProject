package com.jompon.kotlinandroidproject.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v4.view.ViewPager
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jompon.kotlinandroidproject.R
import com.jompon.kotlinandroidproject.adapter.ScreenSlidePagerAdapter
import com.jompon.kotlinandroidproject.base.BaseFragment
import com.jompon.kotlinandroidproject.model.Gallery
import com.jompon.kotlinandroidproject.view.ZoomOutPageTransformer
import kotlinx.android.synthetic.main.fragment_slideshow.*
import com.jompon.kotlinandroidproject.activity.MainActivity
import android.view.animation.AnimationUtils
import android.view.animation.Animation
import java.util.*
import android.R.attr.delay
import com.jompon.kotlinandroidproject.R.id.viewPager
import kotlin.collections.ArrayList


/**
 * Created by Jompon on 12/30/2017.
 */
class SlideshowFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var mContext: Context
    private lateinit var galleries: ArrayList<Gallery>
    private lateinit var mHandler: Handler
    private val slideshowDuration = 5000L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = activity!!
        galleries = ArrayList()
        mHandler = Handler()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_slideshow, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBindingData()
    }

    override fun setBindingData() {

        val photos: Array<String> = resources.getStringArray(R.array.gallery_urls)
        var i = 0
        while(i<26)
        {
            if( i%2 == 0 )  galleries.add(Gallery(photos[i%photos.size], (i+++65).toChar() + ""))
            else            galleries.add(Gallery(photos[i%photos.size], (i+++65).toChar() + "", getString(R.string.gallery_detail)))
        }

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary)
        swipeRefreshLayout.setOnRefreshListener(this)
        onRefresh()
    }

    private fun update(galleries :ArrayList<Gallery>?)
    {
        if( viewPager.adapter == null && galleries != null ){
            val screenSlidePagerAdapter = ScreenSlidePagerAdapter(fragmentManager!!, galleries)
            viewPager.setPageTransformer(true, ZoomOutPageTransformer())
            viewPager.addOnPageChangeListener(SlideshowOnPageChangeListener())
            viewPager.adapter = screenSlidePagerAdapter
            txtPageNum.text = String.format(mContext.getString(R.string.slideshow_page), viewPager.currentItem+1)
            imgArrowLeft.setOnClickListener(ArrowOnClickListener())
            imgArrowRight.setOnClickListener(ArrowOnClickListener())
        } else {
            viewPager.adapter?.notifyDataSetChanged()
        }
        if( viewPager.currentItem == 0 )                                 imgArrowLeft.visibility = View.INVISIBLE
        if( viewPager.currentItem == galleries?.size?.minus(1) )    imgArrowRight.visibility = View.INVISIBLE
        swipeRefreshLayout.isRefreshing = false
    }

    override fun onRefresh() {
        swipeRefreshLayout.isRefreshing = true
        mHandler.removeCallbacks(slideshowRunnable)
        update(galleries)
    }

    private val slideshowRunnable: Runnable = object: Runnable {

        override fun run() {

            viewPager.currentItem = (viewPager.currentItem+1)%(viewPager.adapter as ScreenSlidePagerAdapter).count
            mHandler.postDelayed(this, slideshowDuration)
        }
    }

    override fun onResume() {
        super.onResume()
        mHandler.postDelayed(slideshowRunnable, slideshowDuration)
    }

    override fun onPause() {
        mHandler.removeCallbacks(slideshowRunnable)
        super.onPause()
    }

    inner class SlideshowOnPageChangeListener : ViewPager.OnPageChangeListener{

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) { }

        override fun onPageSelected(position: Int) {
            if (position == 0)
                imgArrowLeft.visibility = View.INVISIBLE
            else
                imgArrowLeft.visibility = View.VISIBLE
            if (position == galleries.size - 1)
                imgArrowRight.visibility = View.INVISIBLE
            else
                imgArrowRight.visibility = View.VISIBLE
            txtPageNum.text = String.format(mContext.getString(R.string.slideshow_page), position+1)
        }

        override fun onPageScrollStateChanged(state: Int) { }
    }

    inner class ArrowOnClickListener : View.OnClickListener{

        override fun onClick(view: View?) {

            if( view == imgArrowLeft ){
                viewPager.currentItem--
            }
            if( view == imgArrowRight ){
                viewPager.currentItem++
            }
        }
    }
}