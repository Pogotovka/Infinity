package com.infinity.my.data.cache.base

import androidx.room.Database
import androidx.room.RoomDatabase
import com.infinity.my.data.cache.IPostDao
import com.infinity.my.data.model.db.PostDB

@Database(
    entities = [PostDB::class],
    version = 1,
    exportSchema = false
)
abstract class MyRoomDatabase : RoomDatabase(){

    abstract fun dbCoordinate() : IPostDao

}