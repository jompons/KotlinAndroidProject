package com.jompon.kotlinandroidproject.base

import android.support.v7.app.AppCompatActivity

/**
 * Created by Jompon on 12/26/2017.
 */
abstract class BaseActivity : AppCompatActivity() {

    companion object {

        private val TAG = BaseActivity::class.java.simpleName
    }

    override fun onResume() {
        super.onResume()
//        BusProvider.getInstance().register(this)
    }

    override fun onPause() {
//        BusProvider.getInstance().unregister(this)
        super.onPause()
    }

    open protected fun setBindingData() {}
}