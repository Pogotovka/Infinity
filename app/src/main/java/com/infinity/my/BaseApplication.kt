package com.infinity.my

import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.infinity.my.data.api.errormanager.errorMapperModule
import com.infinity.my.data.api.repositories.impl.mediaProviderModule
import com.infinity.my.data.api.service.base.networkModule
import com.infinity.my.di.roomDBModule
import com.infinity.my.ui.activitymain.activityViewModule
import com.infinity.my.ui.activitymain.fragmentlistpost.listPostModule
import com.infinity.my.ui.activitymain.fragmentlistpost.postAdapterModule
import com.infinity.my.ui.newpost.fragmentchooseimage.chooseImageModule
import com.infinity.my.ui.newpost.fragmentchooseimage.folderAdapterModule
import com.infinity.my.ui.newpost.fragmentchooseimage.galleryAdapterModule
import com.infinity.my.ui.newpost.fragmentpostdescription.postDescriptionModule
import com.infinity.my.ui.newpost.fragmenttakepicture.takePictureModule
import com.infinity.my.utils.networkHandler
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class BaseApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)

        setupKoin()

        AndroidThreeTen.init(this)

    }

    private fun setupKoin() {
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@BaseApplication)
            modules(
                listOf(
                    networkModule,
                    networkHandler,
                    errorMapperModule,
                    roomDBModule,
                    //view modules
                    activityViewModule,
                    listPostModule,
                    chooseImageModule,
                    takePictureModule,
                    postDescriptionModule,
                    //adapters
                    postAdapterModule,
                    galleryAdapterModule,
                    folderAdapterModule,
                    //gallery
                    mediaProviderModule,
                )
            )
        }
    }
}