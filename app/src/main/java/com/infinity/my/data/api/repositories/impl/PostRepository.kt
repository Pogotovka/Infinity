package com.infinity.my.data.api.repositories.impl

import com.infinity.my.data.Resource
import com.infinity.my.data.api.errormanager.processCall
import com.infinity.my.data.api.repositories.interfaces.IPostRepository
import com.infinity.my.data.api.service.IPostService
import com.infinity.my.data.cache.IPostDao
import com.infinity.my.data.model.db.PostDB
import com.infinity.my.data.model.db.toPostDB
import com.infinity.my.data.model.dto.Post
import com.infinity.my.data.model.dto.Post.Companion.toPost
import com.infinity.my.data.model.dto.ResponsePost
import com.infinity.my.utils.NetworkHandler
import com.infinity.my.utils.castTo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PostRepository(
    private val networkHandler: NetworkHandler,
    private val remoteRepository: IPostService,
    private val iPostDao: IPostDao,
) : IPostRepository {

    override suspend fun sendPostToServer(deviceId: String, postDB: PostDB): Flow<Resource<PostDB>> =
        flow {
            emit(
                when (val result = processCall(networkHandler) { remoteRepository.sendPostToServer(deviceId, postDB.toPost()) }) {
                    is ResponsePost -> {
                        iPostDao.delete(postDB)
                        postDB.copy(isUploaded = true, id = result.name).let {
                            iPostDao.insert(it)
                            Resource.Success(it)
                        }
                    }
                    else -> Resource.DataError(result?.castTo<Int>())
                }
            )
        }

    override suspend fun getAllPost(deviceId: String): Flow<Resource<List<PostDB>>> =
        flow {
            println("cache")
            emit(getListPostFromDb())
            emit(when (val result = processCall(networkHandler) { remoteRepository.getAllPost(deviceId) }) {
                is Map<*, *> -> {
                    val s = result.castTo<Map<String, Post>>().map { it.toPostDB() }
                    iPostDao.insert(s)
                    println("server")
                    getListPostFromDb()
                }
                else -> Resource.DataError(result?.castTo<Int>())
            })
        }

    override suspend fun getListPostFromDb(): Resource<List<PostDB>> = Resource.Success(iPostDao.getListPost())

    override suspend fun savePostToDb(post: PostDB): Flow<Resource.Success<PostDB>> = flow {
        iPostDao.insert(post)
        emit(Resource.Success(post))
    }
}