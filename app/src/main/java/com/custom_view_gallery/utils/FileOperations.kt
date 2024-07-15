package com.custom_view_gallery.utils

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import com.custom_view_gallery.model.Media


object FileOperations {


    private val URI = check29Above {
        MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
    } ?: MediaStore.Images.Media.EXTERNAL_CONTENT_URI


    private val projection = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.RELATIVE_PATH,
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.SIZE,
        MediaStore.Images.Media.MIME_TYPE,
        MediaStore.Images.Media.WIDTH,
        MediaStore.Images.Media.HEIGHT,
        MediaStore.Images.Media.DATE_MODIFIED,

        )

    fun queryImagesOnDevice(context: Context): MutableList<Media> {

        val images = mutableListOf<Media>()

//        withContext(Dispatchers.IO) {


        context.contentResolver.query(
            URI,
            projection, null, null, "${MediaStore.Images.Media.DATE_MODIFIED} DESC"
        )?.use { cursor ->

            while (cursor.moveToNext()) {
                val id =
                    cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                val path =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.RELATIVE_PATH))
                val name =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
                val size =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE))
                val mimeType =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE))
                val width =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH))
                val height =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT))
                val date =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED))

                val uri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                // Discard invalid images that might exist on the device
                if (size == null) {
                    continue
                }

                images += Media(id, uri.toString(), path, name, size, mimeType, width, height, date)

            }
            cursor.close()
        }
//        }

        return images
    }


}