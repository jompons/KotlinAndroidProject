package com.jompon.kotlinandroidproject

import android.content.Context
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.jompon.kotlinandroidproject.base.BaseFragment
import com.jompon.kotlinandroidproject.model.Gallery
import kotlinx.android.synthetic.main.fragment_gallery.*

/**
 * Created by Jompon on 12/26/2017.
 */
class GalleryFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener, OnItemClickListener{

    private var mContext :Context? = null
    private var galleries :ArrayList<Gallery>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = activity
        galleries = ArrayList()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater?.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBindingData()
    }

    override fun setBindingData() {

        var i = 0
        while(i<26)
        {
            galleries?.add(Gallery(null, (i+++65).toChar() + ""))
        }

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary)
        swipeRefreshLayout.setOnRefreshListener(this)
        onRefresh()
    }

    private fun update(galleries :ArrayList<Gallery>?)
    {
        if( recyclerView.adapter == null && galleries != null ){
            val galleryAdapter = GalleryAdapter(galleries)
            galleryAdapter.setOnItemClickListener(this)
            recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.itemAnimator = DefaultItemAnimator()
            recyclerView.adapter = galleryAdapter
        } else {
            recyclerView.adapter.notifyDataSetChanged()
        }
        swipeRefreshLayout.isRefreshing = false
    }

    override fun onRefresh() {

        swipeRefreshLayout.isRefreshing = true
        update(galleries)
    }

    override fun setOnItemClickListener(v: View, position: Int) {

        Toast.makeText(mContext, "Click!!", Toast.LENGTH_LONG).show()
    }
}