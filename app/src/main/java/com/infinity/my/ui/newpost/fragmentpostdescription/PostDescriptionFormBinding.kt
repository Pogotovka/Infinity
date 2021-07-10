package com.infinity.my.ui.newpost.fragmentpostdescription

import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.infinity.my.data.model.ImagePath
import com.infinity.my.ui.base.AbstractFormBinding

class PostDescriptionFormBinding : AbstractFormBinding() {

    override fun onLoad(data: Any?) {

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

    private var _titlePost: String = ""
    var titlePost: String
        @Bindable get() {
            return _titlePost
        }
        set(value) {
            _titlePost = value
            notifyPropertyChanged(BR.titlePost)
        }

    private var _descriptionPost: String = ""
    var descriptionPost: String
        @Bindable get() {
            return _descriptionPost
        }
        set(value) {
            _descriptionPost = value
            notifyPropertyChanged(BR.descriptionPost)
        }

}