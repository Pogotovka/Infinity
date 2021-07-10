package com.infinity.my.utils

import androidx.lifecycle.Observer

open class Event<out T>(private val value: T) {
    var pending = true
        private set

    fun getIfPending(): T? {
        return if (pending) {
            pending = false
            value
        } else {
            null
        }
    }

    fun peek(): T = value
}

class EventObserver<T>(private val observe: (T) -> Unit) : Observer<Event<T>> {
    override fun onChanged(event: Event<T>?) {
        event?.getIfPending()?.let { value ->
            observe(value)
        }
    }
}