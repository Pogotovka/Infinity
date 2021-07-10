package com.infinity.my.data.model.db

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.infinity.my.data.model.dto.Post
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "post")
data class PostDB(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "timestamp")
    val timestamp: Long = 0,

    @ColumnInfo(name = "title")
    val title: String? = null,

    @ColumnInfo(name = "description")
    val description: String? = null,

    @ColumnInfo(name = "imageUrl")
    val imageUrl: String? = null,

    @ColumnInfo(name = "attachmentName")
    val attachmentName: String = "",

    @ColumnInfo(name = "attachmentExtension")
    val attachmentExtension: String? = null,

    @ColumnInfo(name = "isUploaded")
    val isUploaded: Boolean = false
) : Parcelable {
    companion object {
        fun emptyPostDB() = PostDB("")
    }
}

fun Map.Entry<String, Post>.toPostDB(): PostDB = PostDB(
    id = key,
    timestamp = value.timestamp,
    title = value.title,
    description = value.description,
    imageUrl = value.originalImageUrl(),
    attachmentName = value.attachmentName,
    attachmentExtension = value.attachmentExtension,
    isUploaded = true,
)
