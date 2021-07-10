package com.infinity.my.ui.base

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.infinity.my.utils.Event
import com.infinity.my.utils.SingleEvent

abstract class BaseViewModel : ViewModel() {

    val onTouchedPrivate: MutableLiveData<Event<View>> = MutableLiveData<Event<View>>()
    val _onTouched: LiveData<Event<View>> = onTouchedPrivate

    val showMessagePrivate: MutableLiveData<SingleEvent<Any>> = MutableLiveData<SingleEvent<Any>>()
    val showMessage: LiveData<SingleEvent<Any>> get() = showMessagePrivate

}