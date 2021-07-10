package com.infinity.my.data.api.repositories.impl

import com.infinity.my.data.Resource
import com.infinity.my.data.api.errormanager.processCall
import com.infinity.my.data.api.repositories.interfaces.IResourcesRepository
import com.infinity.my.data.api.service.IResourcesService
import com.infinity.my.data.model.dto.ResponseUpload
import com.infinity.my.utils.NetworkHandler
import com.infinity.my.utils.castTo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.RequestBody

class ResourcesRepository(private val iResourcesService: IResourcesService,
                          private val networkHandler: NetworkHandler) : IResourcesRepository {

    override suspend fun uploadResources(attachmentName: String, body: RequestBody): Flow<Resource<ResponseUpload>> =
            flow {
                emit(when (val result = processCall(networkHandler) {
                    iResourcesService.uploadResources(
                            uploadType = "multipart",
                            name = attachmentName,
                            file = body)
                }) {
                    is ResponseUpload -> Resource.Success(result.castTo<ResponseUpload>())
                    else -> Resource.DataError(result?.castTo<Int>())
                })
            }
}