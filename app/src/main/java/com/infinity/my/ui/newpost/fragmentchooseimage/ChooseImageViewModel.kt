package com.infinity.my.ui.newpost.fragmentchooseimage

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.infinity.my.data.api.repositories.interfaces.IMediaProvider
import com.infinity.my.data.model.dto.GalleryFolder
import com.infinity.my.ui.base.BaseViewModel
import com.infinity.my.ui.base.WidthFormBinding
import com.infinity.my.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ChooseImageViewModel(private val mediaProvider: IMediaProvider) : BaseViewModel(), WidthFormBinding<ChooseImageFormBinding> {

    override val formBinding: ChooseImageFormBinding = ChooseImageFormBinding()

    private val galleryResourcesPrivate = MutableLiveData<List<GalleryFolder>>()
    val galleryResources: LiveData<List<GalleryFolder>> = galleryResourcesPrivate

    fun loadGallery() {
        viewModelScope.launch(Dispatchers.IO) {
            mediaProvider.galleryAlbums()
                .map {
                    it.groupBy { g -> g.folderName }
                        .map { folder ->
                            GalleryFolder(
                                folder.key,
                                folder.value.first().thumb,
                                folder.value
                            )
                        }
                }
                .collect {
                        galleryResourcesPrivate.postValue(it)
                }
        }
    }

    fun onClick(v: View) {
        onTouchedPrivate.postValue(Event(v))
    }
}