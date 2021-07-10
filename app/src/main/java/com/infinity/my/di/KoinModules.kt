package com.infinity.my.di

import androidx.room.Room
import com.infinity.my.data.cache.base.MyRoomDatabase
import com.infinity.my.utils.Constants.DB_NAME
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val roomDBModule = module {
    single {
        Room.databaseBuilder(get(), MyRoomDatabase::class.java, DB_NAME)
            .addMigrations()
            .build()
    }
    single { get<MyRoomDatabase>().dbCoordinate() }
}