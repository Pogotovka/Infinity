package com.infinity.my.data.api.service

import com.infinity.my.data.model.dto.ResponseUpload
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url

interface IResourcesService {

    @POST
    suspend fun uploadResources(
            @Url base: String = UPLOAD_RESOURCES,
            @Query("uploadType") uploadType: String,
            @Query("name") name: String,
            @Body file: RequestBody): Response<ResponseUpload>

    companion object {
        private const val UPLOAD_RESOURCES = "https://firebasestorage.googleapis.com/v0/b/tracker-29862.appspot.com/o"
    }
}