package com.custom_view_gallery.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.custom_view_gallery.R
import com.custom_view_gallery.databinding.AlbumViewRvItemBinding
import com.custom_view_gallery.model.Media

class AlbumViewAdapter(val context: Context,val callback:(Int)->Unit) :RecyclerView.Adapter<AlbumViewAdapter.AlbumViewHolder>() {


    private val mediaList  = ArrayList<Media>()

    fun addMediaList(mediaList:ArrayList<Media>){
        this.mediaList.addAll(mediaList)
        notifyDataSetChanged()
    }

    fun getMediaList():ArrayList<Media>{
        return mediaList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
       return AlbumViewHolder(AlbumViewRvItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val media = mediaList[position]

        Glide.with(context)
            .load(media.uri)
            .placeholder(R.drawable.images)
            .into( holder.binding.imgView)

        val ratio = media.width!!.toFloat()/ media.height!!.toFloat()

        val d = 50
       val  width = (d * ratio  ).toInt() ;
        val height = 50;



        holder.binding.root.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.lazy_scroll)





        holder.binding.root.setOnClickListener {
            callback.invoke(position)
        }

    }

    override fun getItemCount(): Int = mediaList.size

    inner  class AlbumViewHolder(val binding: AlbumViewRvItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun getItemId(position: Int): Long {
        return mediaList[position].id  // Replace with your actual item ID logic
    }
}