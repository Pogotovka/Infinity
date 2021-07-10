package com.infinity.my.data.model.dto

data class MediaItem(
        val folderName: String = "",
        val name: String = "",
        val thumb: String = ""
)

data class GalleryFolder(
        val folderName: String = "",
        val pathFolder: String = "",
        val mediaItems: List<MediaItem> = emptyList()
)