package com.infinity.my.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.infinity.my.data.model.db.PostDB
import com.infinity.my.ui.newpost.ActivityNewPost

class HandlePostContract : ActivityResultContract<PostDB?, PostDB?>() {

    override fun createIntent(context: Context, input: PostDB?): Intent =
        Intent(context, ActivityNewPost::class.java)

    override fun parseResult(resultCode: Int, intent: Intent?): PostDB? {
        if (resultCode != Activity.RESULT_OK) {
            return null
        }
        return intent?.getParcelableExtra(POST_KEY)
    }

    companion object {
        const val POST_KEY = "post_key"
    }
}