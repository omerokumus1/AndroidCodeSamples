package com.omerokumus.androidcodesamples.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omerokumus.androidcodesamples.data.Repository
import com.omerokumus.androidcodesamples.data.model.Album
import com.omerokumus.androidcodesamples.data.response.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _uiData = MutableLiveData<String>()
    val uiData: LiveData<String> = _uiData

    fun loadData() {
        viewModelScope.launch {
            // This block runs on Main thread
            Log.d(
                "Thread running:",
                "MainViewModel: ${Thread.currentThread().name}"
            )
            _isLoading.value = true
            val result = repository.fetchAlbum(albumId = 1)
            _isLoading.value = false
            _uiData.value = "Fetched data count: " + result.photoList.size
        }
    }

    private suspend fun processData(
        data: List<Photo>
    ) = withContext(Dispatchers.Default) {
        // Simulate heavy processing (e.g., complex parsing or computation)
        delay(5000) // Simulate a delay for processing
        Album(data.reversed()) // just a mock
    }
}