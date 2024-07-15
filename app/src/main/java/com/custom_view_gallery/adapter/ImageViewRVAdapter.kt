package com.custom_view_gallery.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.custom_view_gallery.R
import com.custom_view_gallery.databinding.ImageViewRvItemBinding
import com.custom_view_gallery.model.Media

class ImageViewRVAdapter(val context: Context,val onClick:(Media)->Unit) :RecyclerView.Adapter<ImageViewRVAdapter.ImageViewHolder>() {

    private val mediaList  = ArrayList<Media>()

    fun addMediaList(mediaList:ArrayList<Media>){
        this.mediaList.addAll(mediaList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(ImageViewRvItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val media = mediaList[position]
        Glide.with(context)
            .load(media.uri)
            .placeholder(R.drawable.images)
            .into( holder.binding.imgView)

        holder.binding.imgView.setOnClickListener {
            onClick.invoke(media)
        }

    }


    public fun setImage(position:Int){

    }

    override fun getItemCount(): Int = mediaList.size

    inner class ImageViewHolder(val binding:ImageViewRvItemBinding):RecyclerView.ViewHolder(binding.root)

}