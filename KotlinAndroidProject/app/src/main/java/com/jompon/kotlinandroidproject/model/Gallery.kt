package com.jompon.kotlinandroidproject.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Jompon on 12/26/2017.
 */
class Gallery(photo: String?, title: String, detail: String) : Parcelable {

    private var photo: String? = null
    private var title: String = ""
    private var detail: String = ""

    init {
        this.photo = photo
        this.title = title
        this.detail = detail
    }

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString()
    )

    constructor(photo: String?, title: String) : this(
            photo,
            title,
            ""
    )

    fun getPhoto(): String? {
        return photo
    }

    fun getTitle(): String {
        return title
    }

    fun getDetail(): String {
        return detail
    }

    fun setPhoto(photo: String?) {
        this.photo = photo
    }

    fun setTitle(title: String) {
        this.title = title
    }

    fun setDetail(detail: String) {
        this.detail = detail
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(photo)
        writeString(title)
        writeString(detail)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Gallery> = object : Parcelable.Creator<Gallery> {
            override fun createFromParcel(source: Parcel): Gallery = Gallery(source)
            override fun newArray(size: Int): Array<Gallery?> = arrayOfNulls(size)
        }
    }
}