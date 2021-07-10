package com.infinity.my.data

sealed class Resource<T>(val data: T? = null, val error: Int? = null) {

    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Success<T>(data: T?) : Resource<T>(data)
    class DataError<T>(error: Int?) : Resource<T>(data = null, error = error)

    override fun toString(): String =
        when (this) {
            is Loading<T> -> "Loading"
            is Success<*> -> "Success[data = $data]"
            is DataError -> "Error[exception = $error]"
        }
}