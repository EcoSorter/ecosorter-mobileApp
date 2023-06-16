package com.dicoding.ecosorter.helper

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @POST("predict")
    fun postImage(
        @Body imageRequestBody: ImageRequestBody
    ): Call<ResponseScanner>
}

data class ImageRequestBody(
    @SerializedName("image")
    val image: String
)