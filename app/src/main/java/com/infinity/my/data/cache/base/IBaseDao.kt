package com.infinity.my.data.cache.base

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery

interface IBaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(obj: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(obj: List<T>)

    @Delete
    suspend fun delete(obj: T)

    @RawQuery
    suspend fun deleteAll(query: SupportSQLiteQuery): Int

    @RawQuery
    suspend fun getAll(query: SupportSQLiteQuery): List<T>
}