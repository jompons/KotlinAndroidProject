package com.jompon.kotlinandroidproject

import android.os.Bundle
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.jompon.kotlinandroidproject.base.BaseActivity
import com.jompon.kotlinandroidproject.model.Gallery
import kotlinx.android.synthetic.main.activity_gallery_detail.*
import kotlinx.android.synthetic.main.layout_gallery.*
import kotlinx.android.synthetic.main.layout_gallery.view.*

/**
 * Created by Jompon on 12/27/2017.
 */
class GalleryDetailActivity : BaseActivity(){

    private var gallery: Gallery? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery_detail)
        setSupportActionBar(toolbar)

        gallery = intent.getParcelableExtra("gallery")
        setTitle(R.string.gallery_title_detail)
        setBindingData()
    }

    override fun setBindingData() {

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
            subtitle = gallery?.getTitle()
        }

        txtTitle.text = gallery?.getTitle()
        txtDetail.text = if( gallery?.getDetail().isNullOrEmpty() ){ getString(R.string.gallery_no_detail) } else String.format(getString(R.string.gallery_detail), gallery?.getTitle())
        Glide.with(this)
                .load(gallery?.getPhoto())
                .into(imgPhoto)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}