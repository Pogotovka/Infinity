package com.infinity.my.ui.newpost.fragmentchooseimage

import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.infinity.my.data.Resource
import com.infinity.my.data.model.ImagePath
import com.infinity.my.ui.base.AbstractFormBinding

class ChooseImageFormBinding : AbstractFormBinding() {

    override fun onLoad(data: Any?) {
        when (data) {
            is Resource.Success<*> -> isLoading.set(false)
            is Resource.DataError<*> -> isLoading.set(false)
            is Resource.Loading<*> -> isLoading.set(true)
            is ImagePath -> imageUri = data
        }
    }

    private var _imageUri: ImagePath = ""
    var imageUri: ImagePath
        @Bindable get() {
            return _imageUri
        }
        set(value) {
            _imageUri = value
            notifyPropertyChanged(BR.imageUri)
        }
}