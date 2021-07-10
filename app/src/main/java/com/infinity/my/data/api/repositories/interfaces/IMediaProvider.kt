package com.infinity.my.data.api.repositories.interfaces

import com.infinity.my.data.model.dto.MediaItem
import kotlinx.coroutines.flow.Flow

interface IMediaProvider {
    fun galleryAlbums(): Flow<MutableList<MediaItem>>
}