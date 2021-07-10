package com.infinity.my.ui.activitymain.fragmentlistpost

import androidx.lifecycle.viewModelScope
import com.infinity.my.data.Resource
import com.infinity.my.data.api.ProgressRequestBody
import com.infinity.my.data.api.errormanager.ErrorMapperInterface
import com.infinity.my.data.api.repositories.interfaces.IPostRepository
import com.infinity.my.data.api.repositories.interfaces.IResourcesRepository
import com.infinity.my.data.model.db.PostDB
import com.infinity.my.data.model.dto.ResponseUpload
import com.infinity.my.ui.base.BaseViewModel
import com.infinity.my.ui.base.WidthFormBinding
import com.infinity.my.utils.SingleEvent
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import java.io.File

class FragmentListPostViewModel(
    private val iPostRepository: IPostRepository,
    private val errorMapperInterface: ErrorMapperInterface,
    private val iResourcesRepository: IResourcesRepository,
    override val formBinding: ListPostFormBinding = ListPostFormBinding()
) : BaseViewModel(), WidthFormBinding<ListPostFormBinding> {

    private val postLiveDataPrivate = MutableStateFlow<List<PostDB>>(listOf())
    val postsLiveData: StateFlow<List<PostDB>> = postLiveDataPrivate.asStateFlow()

    private val _uploadedPost = MutableStateFlow<PostDB?>(PostDB.emptyPostDB())
    val uploadedPost: StateFlow<PostDB?> = _uploadedPost.asStateFlow()

    private val progressUploadAttachmentPrivate = MutableStateFlow(0)
    val progressUploadAttachment: StateFlow<Int> = progressUploadAttachmentPrivate.asStateFlow()

    fun getAllPost() {
        formBinding.onLoad(Resource.Loading(null))

        viewModelScope.launch(IO) {
            iPostRepository.getAllPost("666")
                .collect {
                    formBinding.onLoad(it)
                    when (it) {
                        is Resource.Success<*> -> postLiveDataPrivate.value = it.data!!
                        is Resource.DataError  -> showMessagePrivate.postValue(SingleEvent(errorMapperInterface.getValue(it.error)))
                        else                   -> Unit
                    }
                }
        }
    }

    private suspend fun uploadResources(mPost: PostDB, fileReqBody: RequestBody): Flow<Resource<ResponseUpload>> =
        iResourcesRepository.uploadResources(mPost.attachmentName, fileReqBody)

    @FlowPreview
    fun uploadFailedPost(post: PostDB) {
        formBinding.onLoad(Resource.Loading(null))

        val mFile = File(post.imageUrl)

        var mPost = post.copy(timestamp = System.currentTimeMillis())

//        val requestBody = ProgressRequestBody("image/${post.attachmentExtension}", mFile) {
//            progressUploadAttachmentPrivate.value = it
//        }

        viewModelScope.launch(IO) {
//            uploadResources(post, requestBody)
//                .flatMapConcat { resUpload ->
//                    mPost = mPost.copy(
//                        attachmentName = resUpload.data?.name ?: "",
//                        attachmentExtension = resUpload.data?.contentType,
//                        imageUrl = resUpload.data?.downloadTokens
//                    )
//                    iPostRepository.sendPostToServer("666", mPost)
//                }.collect {
//                    formBinding.onLoad(it)
//                    when (it) {
//                        is Resource.Success   -> _uploadedPost.value = it.data
//                        is Resource.DataError -> showMessagePrivate.postValue(SingleEvent(errorMapperInterface.getValue(it.error)))
//                        else                  -> Unit
//                    }
//                }
        }
    }
}