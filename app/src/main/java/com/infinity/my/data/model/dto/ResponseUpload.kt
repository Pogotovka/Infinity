package com.infinity.my.data.model.dto

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.squareup.moshi.Json

@Keep
data class ResponseUpload(
    @Json(name = "name")
    @Expose
    val name: String,
    @Json(name = "bucket")
    @Expose
    val bucket: String,
    @Json(name = "generation")
    @Expose
    val generation: String,
    @Json(name = "metageneration")
    @Expose
    val metageneration: String,
    @Json(name = "contentType")
    @Expose
    val contentType: String,
    @Json(name = "timeCreated")
    @Expose
    val timeCreated: String,
    @Json(name = "updated")
    @Expose
    val updated: String,
    @Json(name = "storageClass")
    @Expose
    val storageClass: String,
    @Json(name = "size")
    @Expose
    val size: String,
    @Json(name = "md5Hash")
    @Expose
    val md5Hash: String,
    @Json(name = "contentEncoding")
    @Expose
    val contentEncoding: String,
    @Json(name = "contentDisposition")
    @Expose
    val contentDisposition: String,
    @Json(name = "crc32c")
    @Expose
    val crc32c: String,
    @Json(name = "etag")
    @Expose
    val etag: String,
    @Json(name = "downloadTokens")
    @Expose
    val downloadTokens: String,
)