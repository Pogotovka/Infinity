package com.infinity.my.ui.base

import androidx.databinding.BaseObservable
import androidx.databinding.ObservableBoolean

abstract class AbstractFormBinding : BaseObservable() {

    val isLoading: ObservableBoolean = ObservableBoolean(false)

    val isAvailableInternetConnection: ObservableBoolean = ObservableBoolean(true)

    abstract fun onLoad(data: Any?)

}

interface WidthFormBinding<FB : BaseObservable> {
    val formBinding: FB
}