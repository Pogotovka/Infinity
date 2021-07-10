package com.infinity.my.data.api.service.base

import com.infinity.my.BuildConfig
import com.infinity.my.data.api.repositories.impl.PostRepository
import com.infinity.my.data.api.repositories.impl.ResourcesRepository
import com.infinity.my.data.api.repositories.interfaces.IPostRepository
import com.infinity.my.data.api.repositories.interfaces.IResourcesRepository
import com.infinity.my.data.api.service.IPostService
import com.infinity.my.data.api.service.IResourcesService
import com.infinity.my.data.api.service.ISession
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single { provideOkHttpClient() }
    single { providePostApi(get()) }
    factory { provideResourcesApi(get()) }
    single { provideLoggingInterceptor() }
    single { provideRetrofit(get()) }
    single<IPostRepository> { PostRepository(get(), get(),get()) }
    single<IResourcesRepository> { ResourcesRepository(get(), get()) }
}


private const val timeoutRead = 30   //In seconds
private const val timeoutConnect = 30   //In seconds
private const val contentType = "Content-Type"
private const val contentTypeValue = "application/json"

private val logger: HttpLoggingInterceptor
    get() {
        val loggingInterceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            loggingInterceptor.apply { level = HttpLoggingInterceptor.Level.BODY }
        }
        return loggingInterceptor
    }

private var headerInterceptor = Interceptor { chain ->
    val original = chain.request()

    val request = original.newBuilder()
            .header(contentType, contentTypeValue)
            .method(original.method, original.body)
            .build()

    chain.proceed(request)
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
            .baseUrl(BuildConfig.HOST_API_RELEASE)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(provideMoshi()))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
}

private fun provideMoshi() = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder().apply {
            addInterceptor(headerInterceptor)
            addInterceptor(logger)
            connectTimeout(timeoutConnect.toLong(), TimeUnit.SECONDS)
            readTimeout(timeoutRead.toLong(), TimeUnit.SECONDS)
        }.build()


fun provideLoggingInterceptor(): HttpLoggingInterceptor {
    val logger = HttpLoggingInterceptor()
    logger.level = HttpLoggingInterceptor.Level.BASIC
    return logger
}

fun providePostApi(retrofit: Retrofit): IPostService = retrofit.create(IPostService::class.java)

fun provideResourcesApi(retrofit: Retrofit): IResourcesService = retrofit.create(IResourcesService::class.java)
