package com.infinity.my.data.model.dto

import com.google.gson.annotations.Expose
import com.infinity.my.data.model.db.PostDB
import com.squareup.moshi.Json

data class Post(
    @Json(name = "id")
    @Expose
    var id: String = "",

    @Json(name = "timestamp")
    @Expose
    var timestamp: Long = 0,

    @Json(name = "title")
    @Expose
    var title: String? = null,

    @Json(name = "description")
    @Expose
    var description: String? = null,

    @Json(name = "imageUrl")
    @Expose
    var imageUrl: String? = null,

    @Json(name = "attachmentName")
    @Expose
    var attachmentName: String = "",

    @Json(name = "attachmentExtension")
    @Expose
    var attachmentExtension: String? = null
) {
    fun originalImageUrl(): String = "https://firebasestorage.googleapis.com/v0/b/tracker-29862.appspot.com/o/${attachmentName}?alt=media&token=${imageUrl}"

    companion object {
        fun PostDB.toPost(): Post {
            return Post(
                id = id,
                timestamp = timestamp,
                title = title,
                description = description,
                imageUrl = imageUrl,
                attachmentName = attachmentName,
                attachmentExtension = attachmentExtension,
            )
        }
    }
}