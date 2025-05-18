package com.omerokumus.androidcodesamples.data

import com.omerokumus.androidcodesamples.data.response.Photo
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("photos")
    suspend fun getData(@Query("albumId") albumId: Int): List<Photo>
}
