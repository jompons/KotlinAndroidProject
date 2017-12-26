package com.jompon.kotlinandroidproject.base

import android.support.v4.app.Fragment
import android.view.KeyEvent

/**
 * Created by Jompon on 12/26/2017.
 */
abstract class BaseFragment : Fragment() {

    open fun onBackPressed(): Boolean {
        return false
    }

    open fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return false
    }

    open protected fun setBindingData() {}
}