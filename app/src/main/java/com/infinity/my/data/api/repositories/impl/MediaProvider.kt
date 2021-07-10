package com.infinity.my.data.api.repositories.impl

import android.content.Context
import android.database.MergeCursor
import android.provider.MediaStore
import com.infinity.my.data.api.repositories.interfaces.IMediaProvider
import com.infinity.my.data.model.dto.MediaItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.dsl.module
import java.util.*

val mediaProviderModule = module {
    single<IMediaProvider> { MediaProvider(get()) }
}

class MediaProvider(private val mContext: Context) : IMediaProvider {

    override fun galleryAlbums(): Flow<MutableList<MediaItem>> = flow {
        val listForSuccess = mutableListOf<MediaItem>()
        runCatching {
            val columns = arrayOf(
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME
            )
            val sortOrder = MediaStore.Images.Media.DATE_TAKEN + " DESC"
            val mArrayOfCursor =
                listOf(
                    mContext.contentResolver.query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        columns,
                        null,
                        null,
                        sortOrder
                    ),
                    mContext.contentResolver.query(
                        MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                        columns,
                        null,
                        null,
                        sortOrder
                    )
                )

            val mCursor = MergeCursor(mArrayOfCursor.toTypedArray())

            mCursor.moveToFirst()
            while (!mCursor.isAfterLast) {
                val path = mCursor
                    .getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA))
                    .toLowerCase(Locale.getDefault())

                val mFolderName = mCursor
                    .getString(mCursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))

                listForSuccess.add(
                    MediaItem(
                        folderName = mFolderName,
                        name = mFolderName,
                        thumb = path,
                    )
                )
                mCursor.moveToNext()
            }
            mCursor.close()
            emit(listForSuccess)
        }
    }
}