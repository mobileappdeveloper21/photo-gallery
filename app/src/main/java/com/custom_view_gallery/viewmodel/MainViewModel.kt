package com.custom_view_gallery.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.custom_view_gallery.model.Media
import com.custom_view_gallery.utils.FileOperations
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _ImagesLiveData = MutableLiveData<MutableList<Media>>()
    val imageMutableData: LiveData<MutableList<Media>> get() = _ImagesLiveData

    fun loadImages() {
        viewModelScope.launch {
            _ImagesLiveData.postValue(FileOperations.queryImagesOnDevice(getApplication<Application>()))
        }
    }
}