package com.jompon.kotlinandroidproject

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jompon.kotlinandroidproject.model.Gallery
import kotlinx.android.synthetic.main.layout_gallery.view.*

/**
 * Created by Jompon on 12/26/2017.
 */
class GalleryAdapter (val galleries: ArrayList<Gallery>?) : RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>(){

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    override fun getItemCount(): Int {
        return galleries?.let{ galleries.size } ?: 0
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {

        holder.itemView.txtTitle.text = galleries?.get(position)?.getTitle()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_gallery, parent, false)
        val galleryViewHolder = GalleryViewHolder(v)
        galleryViewHolder.setOnItemClickListener(onItemClickListener)
        return galleryViewHolder
    }

    class GalleryViewHolder(itemView :View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private var onItemClickListener: OnItemClickListener? = null

        init {
            itemView.setOnClickListener(this)
        }

        fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
            this.onItemClickListener = onItemClickListener
        }

        override fun onClick(v: View?) {
            if( v == itemView ){
                this.onItemClickListener?.setOnItemClickListener(itemView, adapterPosition)
            }
        }
    }
}