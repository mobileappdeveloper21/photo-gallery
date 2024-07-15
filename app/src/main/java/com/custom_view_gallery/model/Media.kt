package com.custom_view_gallery.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Media(
    val id: Long,
    val uri: String,
    val path: String,
    val name: String,
    val size: String,
    val mimeType: String,
    val width: String?,
    val height: String?,
    val date: String,
): Parcelable