package com.custom_view_gallery.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.custom_view_gallery.R
import com.custom_view_gallery.databinding.ShortImageRvItemBinding
import com.custom_view_gallery.model.Media

class ShortImageViewAdapter(val callback:(Int)->Unit):RecyclerView.Adapter<ShortImageViewAdapter.ShortImageViewHolder>() {

    private val mediaList  = ArrayList<Media>()


    fun addMediaList(mediaList:ArrayList<Media>){
        this.mediaList.addAll(mediaList)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortImageViewHolder {
        return ShortImageViewHolder(ShortImageRvItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ShortImageViewHolder, position: Int) {
        val media = mediaList[position]

        Log.d("find_error", "onBindViewHolder: --->"+position)
        Glide.with(holder.binding.imgView.context)
            .load(media.uri)
            .placeholder(R.drawable.images)
            .into( holder.binding.imgView)

        holder.binding.root.setOnClickListener {
            callback.invoke(position)
        }
    }

    override fun getItemCount(): Int =mediaList.size

    inner class ShortImageViewHolder(val binding:ShortImageRvItemBinding):RecyclerView.ViewHolder(binding.root)

}