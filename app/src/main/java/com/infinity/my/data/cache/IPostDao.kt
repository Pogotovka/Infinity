package com.infinity.my.data.cache

import androidx.room.Dao
import androidx.room.Query
import com.infinity.my.data.cache.base.IBaseDao
import com.infinity.my.data.model.db.PostDB

@Dao
interface IPostDao : IBaseDao<PostDB> {

    @Query(
        """SELECT * FROM post 
                 ORDER BY timestamp DESC"""
    )
    suspend fun getListPost(): List<PostDB>

    @Query(
        """SELECT * FROM post
                 WHERE id = :id
                 ORDER BY timestamp DESC"""
    )
    suspend fun findById(id: String): PostDB
}