package com.jompon.kotlinandroidproject.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.jompon.kotlinandroidproject.R
import com.jompon.kotlinandroidproject.activity.GalleryDetailActivity
import com.jompon.kotlinandroidproject.base.BaseFragment
import com.jompon.kotlinandroidproject.model.Gallery
import com.jompon.kotlinandroidproject.util.Constant
import kotlinx.android.synthetic.main.layout_gallery.*


/**
 * Created by Jompon on 12/30/2017.
 */
class ScreenSlideshowFragment : BaseFragment(), View.OnClickListener{

    private lateinit var mContext: Context
    private lateinit var gallery: Gallery
    companion object{
        fun newInstance(gallery: Gallery): ScreenSlideshowFragment {
            val f = ScreenSlideshowFragment()

            val args = Bundle()
            args.putParcelable(Constant.KEY_GALLERY, gallery)
            f.arguments = args

            return f
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = activity!!
        gallery = arguments!!.getParcelable(Constant.KEY_GALLERY)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_screenslideshow, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBindingData()
    }

    override fun setBindingData() {

        txtTitle.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        txtTitle.text = gallery.getTitle()
        Glide.with(mContext)
                .load(gallery.getPhoto())
                .into(imgPhoto)
    }

    override fun onClick(p0: View?) {

        val intent = Intent(mContext, GalleryDetailActivity::class.java)
        intent.putExtra(Constant.KEY_GALLERY, gallery)
        startActivity(intent)
    }
}