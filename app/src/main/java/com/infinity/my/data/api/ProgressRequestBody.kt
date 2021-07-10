package com.infinity.my.data.api

import com.infinity.my.data.model.db.PostDB
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream

class ProgressRequestBody(
    private val mediaType: String,
    private val mFile: File,
    private val mPostDB: PostDB,
    private val callbackProgressLoaded: ((PostDB) -> Unit)
) : RequestBody() {

    companion object {
        private const val DEFAULT_BUFFER_SIZE = 1024
    }

    override fun contentLength(): Long = mFile.length()

    override fun contentType(): MediaType? = mediaType.toMediaTypeOrNull()

    override fun writeTo(sink: BufferedSink) {
        val inputStream = FileInputStream(mFile)
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        var uploaded: Long = 0
        val fileSize = mFile.length()

        while (true) {
            val read = inputStream.read(buffer)
            if (read == -1) break
            uploaded += read
            sink.write(buffer, 0, read)
            val progress = (uploaded / fileSize.toDouble() * 100).toInt()
//            callbackProgressLoaded.invoke(mPostDB.apply { progressUpload = progress })
        }
    }
}
