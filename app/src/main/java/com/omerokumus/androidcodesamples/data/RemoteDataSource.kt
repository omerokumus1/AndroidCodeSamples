package com.omerokumus.androidcodesamples.data

import android.util.Log
import com.omerokumus.androidcodesamples.data.response.Photo

class RemoteDataSource(private val api: ApiService) {
    // userId is useless, it is just for demonstration
    suspend fun fetchData(albumId: Int, userId: Int): List<Photo> {
        Log.d(
            "Thread running:",
            "RemoteDataSource: ${Thread.currentThread().name}"
        )
        return api.getData(albumId)
    }
}
