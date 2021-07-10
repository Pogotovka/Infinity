package com.infinity.my.data.api.repositories.interfaces

import com.infinity.my.data.Resource
import com.infinity.my.data.model.db.PostDB
import com.infinity.my.data.model.dto.ResponsePost
import kotlinx.coroutines.flow.Flow

interface IPostRepository {

    suspend fun getAllPost(deviceId: String): Flow<Resource<List<PostDB>>>

    suspend fun sendPostToServer(deviceId: String, postDB: PostDB): Flow<Resource<PostDB>>

    suspend fun getListPostFromDb(): Resource<List<PostDB>>

    suspend fun savePostToDb(post: PostDB): Flow<Resource<PostDB>>
}