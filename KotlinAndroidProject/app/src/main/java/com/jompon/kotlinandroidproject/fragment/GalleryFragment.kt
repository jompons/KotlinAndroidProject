package com.jompon.kotlinandroidproject.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jompon.kotlinandroidproject.impl.OnItemClickListener
import com.jompon.kotlinandroidproject.R
import com.jompon.kotlinandroidproject.activity.GalleryDetailActivity
import com.jompon.kotlinandroidproject.adapter.GalleryAdapter
import com.jompon.kotlinandroidproject.base.BaseFragment
import com.jompon.kotlinandroidproject.model.Gallery
import kotlinx.android.synthetic.main.fragment_gallery.*

/**
 * Created by Jompon on 12/26/2017.
 */
class GalleryFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {

    private lateinit var mContext: Context
    private var galleries: ArrayList<Gallery>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = activity!!
        galleries = ArrayList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_gallery, container, false)
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
            if( i%2 == 0 )  galleries?.add(Gallery(photos[i%photos.size], (i+++65).toChar() + ""))
            else            galleries?.add(Gallery(photos[i%photos.size], (i+++65).toChar() + "", getString(R.string.gallery_detail)))
        }

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary)
        swipeRefreshLayout.setOnRefreshListener(this)
        onRefresh()
    }

    private fun update(galleries :ArrayList<Gallery>?)
    {
        if( recyclerView.adapter == null && galleries != null ){
            val galleryAdapter = GalleryAdapter(mContext, galleries)
            galleryAdapter.setOnItemClickListener(this)
            recyclerView.addItemDecoration(DividerItemDecoration(mContext, GridLayoutManager.VERTICAL))
            recyclerView.addItemDecoration(DividerItemDecoration(mContext, GridLayoutManager.HORIZONTAL))
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = GridLayoutManager(mContext, 2)
            recyclerView.itemAnimator = DefaultItemAnimator()
            recyclerView.adapter = galleryAdapter
        } else {
            recyclerView.adapter?.notifyDataSetChanged()
        }
        swipeRefreshLayout.isRefreshing = false
    }

    override fun onRefresh() {

        swipeRefreshLayout.isRefreshing = true
        update(galleries)
    }

    override fun setOnItemClickListener(v: View, position: Int) {

        val intent = Intent(mContext, GalleryDetailActivity::class.java)
        intent.putExtra("gallery", galleries?.get(position))
        intent.putExtra("title", galleries?.get(position)?.getTitle())
        startActivity(intent)
    }
}