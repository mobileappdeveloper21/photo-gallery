package com.custom_view_gallery.ui

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.custom_view_gallery.R
import com.custom_view_gallery.adapter.AlbumViewAdapter
import com.custom_view_gallery.databinding.ActivityAlbumViewBinding
import com.custom_view_gallery.model.Media
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.math.log

class AlbumViewActivity : AppCompatActivity() {

    companion object{
        const val ALBUM_MEDIA_LIST_INTENT = "Album_Media_List"
        const val ACTIVITY_TITLE = "ACTIVITY_TITLE"
    }

    private lateinit var binding:ActivityAlbumViewBinding
    private lateinit var albumViewAdapter: AlbumViewAdapter

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlbumViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListener()
        setAdapter()
        setDataFromIntent()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun setDataFromIntent() {

        var mediaList:ArrayList<Media>? = arrayListOf()
        val gson = Gson()
        val type = object : TypeToken<List<Media?>?>() {}.type
        val data = intent.getStringExtra(ALBUM_MEDIA_LIST_INTENT)
        mediaList =gson.fromJson(data,type)

        //set Adapter
        mediaList?.let {
            albumViewAdapter.addMediaList(it)
        }


        binding.albumTitle.text = intent.getStringExtra(ACTIVITY_TITLE)

    }

    private fun setAdapter() {
        albumViewAdapter = AlbumViewAdapter(this){
            moveToImageViewActivity(it)

        }
        binding.albumRV.apply {
            adapter = albumViewAdapter
            layoutManager = StaggeredGridLayoutManager(2,RecyclerView.VERTICAL)
            hasFixedSize()
        }
    }

    private fun moveToImageViewActivity(position: Int) {
        startActivity(Intent(this,ImageViewActivity::class.java).apply {
            putExtra(ALBUM_MEDIA_LIST_INTENT,Gson().toJson(albumViewAdapter.getMediaList()))
            putExtra(ImageViewActivity.CURRENT_SELECTED,position)
        })
    }

    private fun setListener() {
        binding.backBtn.setOnClickListener {
            finish()
        }
    }

}