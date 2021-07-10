package com.infinity.my.ui.newpost

import android.view.View
import com.infinity.my.R
import com.infinity.my.databinding.ActivityNewPostBinding
import com.infinity.my.ui.base.BaseActivity

class ActivityNewPost : BaseActivity<ActivityNewPostBinding>(R.layout.activity_new_post) {

    override fun afterCreateView() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    override fun initObservers() {

    }
}
