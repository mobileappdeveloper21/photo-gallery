package com.custom_view_gallery.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale




inline fun <T> check29Above(check: () -> T): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        check()
    } else {
        null
    }

}

const val READ_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE
const val WRITER_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE


fun havePermission(context: Context, permission: String): Boolean {

    return ContextCompat.checkSelfPermission(
        context,
        permission
    ) == PackageManager.PERMISSION_GRANTED

}


fun checkingPermission(
    context: Context,
    permissionLauncher: ActivityResultLauncher<Array<String>>
): Boolean {


    var storagePermissionArray = mutableListOf<String>()

    var readPermission = havePermission(context, READ_PERMISSION)
    var writePermission =
        havePermission(context, WRITER_PERMISSION) || check29Above { true } ?: false

    if (!readPermission) {
        storagePermissionArray.add(READ_PERMISSION)

    }

    if (!writePermission) {

        storagePermissionArray.add(WRITER_PERMISSION)
    }


    if (storagePermissionArray.isNotEmpty()) {

        permissionLauncher.launch(storagePermissionArray.toTypedArray())
        return false
    }

    return true
}


fun formatDate(unixTimestamp: Long): String? {
    val date = Date(unixTimestamp * 1000L) // Convert seconds to milliseconds
    val sdf = SimpleDateFormat("yyyy MMM dd ", Locale.getDefault())
    return sdf.format(date)
}

fun formatDateWithTime(unixTimestamp: Long):String?{
    val date = Date(unixTimestamp * 1000L) // Convert seconds to milliseconds
    val sdf = SimpleDateFormat("yyyy MMM dd, HH:ss", Locale.getDefault())
    return sdf.format(date)
}

fun formatTime(unixTimestamp: Long):String?{
    val date = Date(unixTimestamp * 1000L) // Convert seconds to milliseconds
    val sdf = SimpleDateFormat("HH:ss", Locale.getDefault())
    return sdf.format(date)
}

fun getFolderNameFromPath(filePath: String): String? {
    val file = File(filePath)
    return if (file.exists()) {
        file.parentFile?.name  // This gives the name of the parent directory
    } else {
        null
    }
}


fun getLastFolderName(path: String): String {
    // Remove trailing slashes if present
    val trimmedPath = path.trimEnd('/')

    // Split the path by '/'
    val parts = trimmedPath.split('/')

    // Get the last part of the split array
    return parts.last()
}

