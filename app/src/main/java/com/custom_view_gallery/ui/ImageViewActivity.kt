package com.custom_view_gallery.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import androidx.recyclerview.widget.SnapHelper
import com.custom_view_gallery.adapter.ImageViewRVAdapter
import com.custom_view_gallery.adapter.ShortImageViewAdapter
import com.custom_view_gallery.customViews.CenterDecoration
import com.custom_view_gallery.customViews.CenterSnapHelper
import com.custom_view_gallery.customViews.CenterZoomLayoutManager
import com.custom_view_gallery.databinding.ActivityImageViewBinding
import com.custom_view_gallery.model.Media
import com.custom_view_gallery.utils.formatDate
import com.custom_view_gallery.utils.formatDateWithTime
import com.custom_view_gallery.utils.formatTime
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class ImageViewActivity : AppCompatActivity() {

    companion object{
        const val CURRENT_SELECTED ="Current_selected"
    }
    private val snapHelper = CenterSnapHelper()
    private lateinit var binding:ActivityImageViewBinding
    private lateinit var shortImageAdapter:ShortImageViewAdapter
    private lateinit var imageViewAdapter:ImageViewRVAdapter

    private lateinit var layoutManagerImageView: LinearLayoutManager
//    private lateinit var centerZoomLayoutManager: CenterZoomLayoutManager

    private var mediaList:ArrayList<Media>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListener()
        setAdapter()
        setDataFromIntent()
    }

    private fun setListener() {
        binding.backBtn.setOnClickListener { finish() }
    }

    private fun setAdapter() {

        val smoothScroller: SmoothScroller = object : LinearSmoothScroller(this) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }
//      centerZoomLayoutManager =   CenterZoomLayoutManager(this@ImageViewActivity,RecyclerView.HORIZONTAL,false)
        shortImageAdapter = ShortImageViewAdapter {position->
            smoothScrolling(position)
            mediaList?.let {
//                setInitImage(it[position])
                smoothScrolling(position)
            }

        }
        binding.albumShortRV.apply {
            adapter = shortImageAdapter
//            layoutManager = centerZoomLayoutManager
            hasFixedSize()
        }

        imageViewAdapter = ImageViewRVAdapter(this){media ->
            hideShowBottomAndTopView(true)
        }
        layoutManagerImageView = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        binding.imageRv.apply {
            adapter = imageViewAdapter
            layoutManager = layoutManagerImageView
            hasFixedSize()
        }

        val snapHelper: SnapHelper = PagerSnapHelper()
//        val snapHelper2: SnapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.imageRv)
//        snapHelper2.attachToRecyclerView(binding.albumShortRV)


        binding.imageRv.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                // Check if the scroll state has settled (stopped)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // RecyclerView has settled, find the index of the centered item
                    val centeredItemPosition = findCenteredItemPosition(layoutManagerImageView)
                    smoothScrolling(centeredItemPosition)
                    // Use centeredItemPosition as needed (e.g., update UI, perform actions)
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })

    }

    private fun setDataFromIntent() {
        val gson = Gson()
        val type = object : TypeToken<List<Media?>?>() {}.type
        val data = intent.getStringExtra(AlbumViewActivity.ALBUM_MEDIA_LIST_INTENT)
        mediaList =gson.fromJson(data,type)

        val currentSelected = intent.getIntExtra(CURRENT_SELECTED,0)
        //set Adapter
        mediaList?.let {
//            binding.albumShortRV.addItemDecoration(CenterDecoration(0))
//            snapHelper.attachToRecyclerView(binding.albumShortRV)
            shortImageAdapter.addMediaList(it)
            imageViewAdapter.addMediaList(it)
            smoothScrolling(currentSelected)
        }

    }

    private fun setInitImage(media: Media) {

          if(!media.name.isNullOrEmpty()){
            binding.imageName.text =   media.name
            binding.dateTV.text = formatDateWithTime(media.date.toLong())
        }else{
            binding.imageName.text =   formatDate(media.date.toLong())
            binding.dateTV.text = formatTime(media.date.toLong())
          }

    }


    private fun smoothScrolling(position:Int){
        mediaList?.let {
            setInitImage(it[position])
        }

        binding.imageRv.scrollToPosition(position)
        binding.albumShortRV.scrollToPosition(position)

//        scrollToCenter(position)
    }

    // Method to find the index of the centered item
    private fun findCenteredItemPosition(layoutManager: LinearLayoutManager): Int {
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

        // Calculate the index of the centered item
        val centeredPosition = (firstVisibleItemPosition + lastVisibleItemPosition) / 2
        return centeredPosition
    }


    /**
     * hide show Bottom And Top View as The image click
     * */
    private fun hideShowBottomAndTopView(isHide:Boolean){

        if(binding.bottomContainer.visibility == View.GONE){
            binding.bottomContainer.visibility = View.VISIBLE
            binding.topMenuContainer.visibility = View.VISIBLE
        }else{
            binding.bottomContainer.visibility = View.GONE
            binding.topMenuContainer.visibility = View.GONE
        }


    }


    private fun scrollToCenter(position: Int) {
        binding.albumShortRV.post {
            val layoutManager = binding.albumShortRV.layoutManager as LinearLayoutManager
            val snapDistance = binding.albumShortRV.width / 2 - binding.albumShortRV.getChildAt(0).width / 2
            layoutManager.scrollToPositionWithOffset(position, snapDistance)
        }
    }

}