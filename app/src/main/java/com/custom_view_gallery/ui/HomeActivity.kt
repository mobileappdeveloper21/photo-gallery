package com.custom_view_gallery.ui

import android.app.RecoverableSecurityException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.custom_view_gallery.R
import com.custom_view_gallery.adapter.CollectionDateRecyclerviewAdapter
import com.custom_view_gallery.databinding.ActivityHomeBinding
import com.custom_view_gallery.model.Media
import com.custom_view_gallery.model.MediaDateCollection
import com.custom_view_gallery.utils.READ_PERMISSION
import com.custom_view_gallery.utils.WRITER_PERMISSION
import com.custom_view_gallery.utils.checkingPermission
import com.custom_view_gallery.utils.getLastFolderName
import com.custom_view_gallery.viewmodel.MainViewModel
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import kotlinx.coroutines.withContext


class HomeActivity : AppCompatActivity() {

    private var hasAllPermission = false
    private lateinit var galleryAdapter:CollectionDateRecyclerviewAdapter
    private val vm: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }
    private lateinit var intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { p ->

            var readPermission = p[READ_PERMISSION] ?: false
            var writePermission = p[WRITER_PERMISSION] ?: false

            if (readPermission) {
                hasAllPermission = true
                loadPhotoFromExternalStorage()
            } else {
                hasAllPermission = false
                Toast.makeText(
                    this@HomeActivity,
                    "Can't read files without permission",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }


    private lateinit var binding:ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hasAllPermission = checkingPermission(this@HomeActivity, permissionLauncher)
        initObserver()
        setUpRv()
        intentSenderLauncher =
            registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
                if(it.resultCode == RESULT_OK){
                    Toast.makeText(this@HomeActivity, "Photo deleted successfully", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this@HomeActivity, "Photo couldn't be  deleted", Toast.LENGTH_SHORT).show()
                }

            }

        if(hasAllPermission){
            loadPhotoFromExternalStorage()
        }
    }

    /**
     * setUp Recyclerview
     * */
    private fun setUpRv() {
        galleryAdapter = CollectionDateRecyclerviewAdapter(this){mediaController,title->
            moveToAlbumViewActivity(mediaController,title)
        }
        binding.galleryViewRV.apply {
            adapter = galleryAdapter
            hasFixedSize()
        }
    }

    /**
     * Move to album view Activity
     * */
    private fun moveToAlbumViewActivity(media: ArrayList<Media>, title: String) {
        startActivity(Intent(this,AlbumViewActivity::class.java).apply {
            putExtra(AlbumViewActivity.ALBUM_MEDIA_LIST_INTENT,Gson().toJson(media))
            putExtra(AlbumViewActivity.ACTIVITY_TITLE,title)
        })
    }


    private fun initObserver() {

        vm.imageMutableData.observe(this) {
            it?.let { media ->
                initRecyclerView(media)
            }

        }

    }

    private suspend fun deletePhotoFromExternalStorage(photoUri: Uri) {
        withContext(Dispatchers.IO) {
            try {
                contentResolver.delete(photoUri, null, null)
            } catch (e: SecurityException) {
                e.printStackTrace()

                val intentSender = when {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                        MediaStore.createDeleteRequest(
                            contentResolver,
                            listOf(photoUri)
                        ).intentSender
                    }
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                        val recoverableSecurityException = e as RecoverableSecurityException
                        recoverableSecurityException.userAction.actionIntent.intentSender
                    }
                    else -> null

                }

                intentSender?.let { sender ->
                    intentSenderLauncher.launch(IntentSenderRequest.Builder(sender).build())
                }
            }
        }
    }

    private fun initRecyclerView(medias: MutableList<Media>) {

        CoroutineScope(Dispatchers.IO).launch {
            val collectionList:ArrayList<MediaDateCollection> = ArrayList()
            val allPhotoList:ArrayList<Media>  = arrayListOf()
            medias.forEach {media->
                allPhotoList.add(media)
                val parentFolderName = getLastFolderName(media.path)
                val containerElement = collectionList.filter { it.collectionName == parentFolderName }
                if(containerElement.isEmpty()){
                    val mediaCollection = MediaDateCollection(
                        collectionName = parentFolderName, collectionDate = media.date ,  collectionList =arrayListOf(media) )
                    collectionList.add(mediaCollection)
                }else{
                    collectionList[0].collectionList.add(media)
                }
            }
            collectionList.add(0, MediaDateCollection(
                collectionName = getString(R.string.all_photo), collectionDate = "" ,  collectionList =allPhotoList))


            runOnUiThread {
                updateRecyclerView(collectionList)
            }
        }


    }

    /**
     * update Recyclerview After fetching image from gallery and make collection as per date
     * */
    fun updateRecyclerView(collectionList:ArrayList<MediaDateCollection>){


        binding.galleryViewRV.apply {
            galleryAdapter.addCollectionList(collectionList);

        }


    }


    fun loadPhotoFromExternalStorage() {
        vm.loadImages()
    }




}