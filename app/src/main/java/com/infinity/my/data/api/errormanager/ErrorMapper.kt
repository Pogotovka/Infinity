package com.infinity.my.data.api.errormanager

import android.content.Context
import android.util.Log
import com.infinity.my.R
import com.infinity.my.data.Resource
import com.infinity.my.utils.NetworkHandler
import org.koin.dsl.module
import retrofit2.Response
import java.io.IOException

val errorMapperModule = module {
    single<ErrorMapperInterface> { ErrorMapper(get()) }
}

const val NO_INTERNET_CONNECTION = -1
const val NETWORK_ERROR = -2

class ErrorMapper(private val context: Context) : ErrorMapperInterface {

    override val errorsMap: Map<Int, String>
        get() = mapOf(
                Pair(NO_INTERNET_CONNECTION, getErrorString(R.string.no_internet)),
                Pair(NETWORK_ERROR, getErrorString(R.string.network_error))
        ).withDefault { getErrorString(R.string.network_error) }

    override fun getValue(errorCode: Int?) = errorsMap.getValue(errorCode ?: 0)

    override fun getErrorString(errorId: Int): String = context.getString(errorId)

}

interface ErrorMapperInterface {
    fun getErrorString(errorId: Int): String
    fun getValue(errorCode: Int?): String
    val errorsMap: Map<Int, String>
}

suspend fun processCall(networkHandler: NetworkHandler, responseCall: suspend () -> Response<*>): Any? {
    if (!networkHandler.isNetworkAvailable()) {
        return NO_INTERNET_CONNECTION
    }
    return try {
        val response = responseCall.invoke()
        val responseCode = response.code()
        if (response.isSuccessful) {
            response.body()
        } else {
            Log.e("api error responseCode", responseCode.toString())
            responseCode
        }
    } catch (e: IOException) {
        Log.e("api error", e.stackTraceToString())
        NETWORK_ERROR
    }
}