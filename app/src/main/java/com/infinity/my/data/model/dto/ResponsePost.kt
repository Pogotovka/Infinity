package com.infinity.my.data.model.dto

import com.squareup.moshi.Json

data class ResponsePost(
    @Json(name = "name")
    val name: String
)