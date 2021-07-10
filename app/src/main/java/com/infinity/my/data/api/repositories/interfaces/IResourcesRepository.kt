package com.infinity.my.data.api.repositories.interfaces

import com.infinity.my.data.Resource
import com.infinity.my.data.model.dto.ResponseUpload
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody

interface IResourcesRepository {
    suspend fun uploadResources(attachmentName: String, body: RequestBody): Flow<Resource<ResponseUpload>>
}
