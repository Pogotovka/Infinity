package com.infinity.my.ui.newpost.fragmentpostdescription

import androidx.lifecycle.viewModelScope
import com.infinity.my.data.Resource
import com.infinity.my.data.api.repositories.interfaces.IPostRepository
import com.infinity.my.data.model.db.PostDB
import com.infinity.my.ui.base.BaseViewModel
import com.infinity.my.ui.base.WidthFormBinding
import com.infinity.my.utils.Event
import com.infinity.my.utils.SingleEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

class PostDescriptionViewModel(
    private val iPostRepository: IPostRepository,
    override val formBinding: PostDescriptionFormBinding = PostDescriptionFormBinding()
) : BaseViewModel(), WidthFormBinding<PostDescriptionFormBinding> {

    private val postSavedPrivate: MutableSharedFlow<Event<Any>> = MutableSharedFlow(replay = 1, extraBufferCapacity = 0, BufferOverflow.DROP_OLDEST)
    val postSaved: SharedFlow<Event<Any>> get() = postSavedPrivate

    fun swapData(imageUrl: String) {
        formBinding.imageUri = imageUrl
    }

    fun isValidData(): Boolean {
        with(formBinding) {
            return when {
                titlePost.isEmpty()       -> {
                    showMessagePrivate.value = SingleEvent("Please set title")
                    false
                }
                descriptionPost.isEmpty() -> {
                    showMessagePrivate.value = SingleEvent("Please set description")
                    false
                }
                imageUri.isEmpty()        -> {
                    showMessagePrivate.value = SingleEvent("Please choose image")
                    false
                }
                else                      -> true
            }
        }
    }

    fun savePost() {
        val mFile = File(formBinding.imageUri)
        val mPost = PostDB(
            id = UUID.randomUUID().toString(),
            title = formBinding.titlePost,
            timestamp = System.currentTimeMillis(),
            description = formBinding.descriptionPost,
            attachmentName = mFile.nameWithoutExtension,
            attachmentExtension = mFile.extension,
            imageUrl = mFile.absolutePath
        )

        formBinding.onLoad(Resource.Loading(null))

        viewModelScope.launch(Dispatchers.IO) {
            iPostRepository.savePostToDb(mPost)
                .collect {
                    when (it) {
                        is Resource.Success   -> postSavedPrivate.emit(Event(Unit))
                        is Resource.DataError -> showMessagePrivate.postValue(SingleEvent("Error to save post"))
                        else                  -> Unit
                    }
                }
        }
    }
}