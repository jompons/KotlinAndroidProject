package com.jompon.kotlinandroidproject.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Jompon on 12/26/2017.
 */
class Gallery(){

    private var photo: String? = null
    private var title: String = ""

    init {
        this.photo = photo
        this.title = title
    }

    constructor(photo: String?, title: String) : this( )
    {
        this.photo = photo
        this.title = title
    }

    fun getPhoto(): String? {
        return photo
    }

    fun getTitle(): String {
        return title
    }

    fun setPhoto(photo: String?) {
        this.photo = photo
    }

    fun setTitle(title: String) {
        this.title = title
    }
}