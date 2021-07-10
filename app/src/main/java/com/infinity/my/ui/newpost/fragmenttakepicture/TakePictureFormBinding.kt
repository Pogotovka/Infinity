package com.infinity.my.ui.newpost.fragmenttakepicture

import androidx.databinding.ObservableBoolean
import com.infinity.my.ui.base.AbstractFormBinding

class TakePictureFormBinding  : AbstractFormBinding() {

    override fun onLoad(data: Any?) {

    }

    val isCameraPermissionAccess : ObservableBoolean = ObservableBoolean(false)
    
    val isCaptureEnabled : ObservableBoolean = ObservableBoolean(false)

}