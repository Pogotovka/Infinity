package com.infinity.my.ui.activitymain.fragmentlistpost

import com.infinity.my.data.Resource
import com.infinity.my.ui.base.AbstractFormBinding

class ListPostFormBinding : AbstractFormBinding() {

    override fun onLoad(data: Any?) {
        when (data) {
            is Resource.Success<*> -> isLoading.set(false)
            is Resource.DataError<*> -> isLoading.set(false)
            is Resource.Loading<*> -> isLoading.set(true)
        }
    }
}