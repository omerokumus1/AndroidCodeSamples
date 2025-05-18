package com.omerokumus.androidcodesamples.data

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.omerokumus.androidcodesamples.data.model.Album
import com.omerokumus.androidcodesamples.data.response.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class Repository(
    // RemoteDataSource uses Retrofit internally
    private val remoteDataSource: RemoteDataSource,
    private val sharedPreferences: SharedPreferences
) {

    suspend fun fetchAlbum(albumId: Int): Album {
        // This block runs on Main thread because of viewModelScope
        delay(3000)
        Log.d(
            "Thread running:",
            "Repository getProcessedData: ${Thread.currentThread().name}"
        )
        // I/O: Read from SharedPreferences (blocking), thus, use IO dispatcher
        val userId = withContext(Dispatchers.IO) {
            // This block runs on IO thread
            Log.d(
                "Thread running:",
                "Repository in withContext lambda: ${Thread.currentThread().name}"
            )
            sharedPreferences.getInt("userId", 1)
        }

        // Retrofit handles threading internally (you don't wrap it)
        val apiResponse = remoteDataSource.fetchData(albumId, userId)

        // CPU-bound processing on the result
        return withContext(Dispatchers.Default) {
            // This block runs on Default thread
            processApiResponse(apiResponse)
        }
    }

    suspend fun getAlbum(albumId: Int) = withContext(Dispatchers.IO) {
        val userId = sharedPreferences.getInt("userId", 1)
        val result = remoteDataSource.fetchData(albumId, userId)
        sharedPreferences.edit { putInt("lastFetchedAlbumId", albumId) }
        result
    }

    private suspend fun processApiResponse(response: List<Photo>): Album {
        Log.d(
            "Thread running:",
            "Repository processApiResponse: ${Thread.currentThread().name}"
        )
        // Simulate heavy processing (e.g., complex parsing or computation)
        delay(5000) // Simulate a delay for processing
        return Album(response.reversed()) // just a mock
    }
}
