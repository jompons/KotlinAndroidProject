package com.jompon.kotlinandroidproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jompon.kotlinandroidproject.base.BaseFragment

/**
 * Created by Jompon on 12/26/2017.
 */
class GalleryFragment : BaseFragment(){

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater?.inflate(R.layout.fragment_gallery, container, false)
        setBindingData()
        return rootView
    }

    override fun setBindingData() {

    }
}