package com.infinity.my.data.api.service

import com.infinity.my.data.model.dto.Post
import com.infinity.my.data.model.dto.ResponsePost
import com.infinity.my.data.model.dto.ResponseUpload
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface IPostService {

    @POST("{deviceId}.json")
    suspend fun sendPostToServer(@Path("deviceId") deviceId: String, @Body post: Post): Response<ResponsePost>

    @GET("{deviceId}.json")
    suspend fun getAllPost(@Path("deviceId") deviceId: String): Response<Map<String, Post>>
}