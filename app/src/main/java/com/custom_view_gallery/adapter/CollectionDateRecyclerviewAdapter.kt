package com.custom_view_gallery.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.custom_view_gallery.databinding.CollectionDateItemRvBinding
import com.custom_view_gallery.model.Media
import com.custom_view_gallery.model.MediaDateCollection

class CollectionDateRecyclerviewAdapter(val context: Context,val onItemClick:(ArrayList<Media>,String)->Unit):RecyclerView.Adapter<CollectionDateRecyclerviewAdapter.CollectionViewHolder>() {

    private val collectionList  = ArrayList<MediaDateCollection>()


     fun addCollectionList(collectionList:ArrayList<MediaDateCollection>){
        this.collectionList.addAll(collectionList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
       return CollectionViewHolder(CollectionDateItemRvBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        val mediaCollection = collectionList[position]

        holder.binding.collectionDate.text =mediaCollection.collectionName
        holder.binding.collectionCount.text = mediaCollection.collectionList.size.toString()

        if(mediaCollection.collectionList.size >=2){
            holder.binding.imvFirst.visibility = View.VISIBLE
            Glide.with(context)
                .load(mediaCollection.collectionList[0].uri)
                .into( holder.binding.imvFirst)
            Glide.with(context)
                .load(mediaCollection.collectionList[1].uri)
                .into( holder.binding.imageSecond)
        }else{
            Glide.with(context)
                .load(mediaCollection.collectionList[0].uri)
                .into( holder.binding.imageSecond)
            holder.binding.imvFirst.visibility = View.GONE
        }

        holder.binding.root.setOnClickListener {
            onItemClick.invoke(mediaCollection.collectionList,mediaCollection.collectionName)
        }

    }

    override fun getItemCount(): Int = collectionList.size

    inner class CollectionViewHolder (val binding:CollectionDateItemRvBinding):RecyclerView.ViewHolder(binding.root)
}