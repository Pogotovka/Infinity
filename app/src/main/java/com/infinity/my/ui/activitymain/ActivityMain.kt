package com.infinity.my.ui.activitymain

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.infinity.my.R
import com.infinity.my.databinding.ActivityMainBinding
import com.infinity.my.ui.base.BaseActivity
import com.infinity.my.ui.base.navigator.goToActivity
import com.infinity.my.ui.newpost.ActivityNewPost
import org.koin.android.viewmodel.ext.android.viewModel

class ActivityMain : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val mViewModel: ActivityMainViewModel by viewModel()

    override fun afterCreateView() {
//        binding.bottomAppBar.setOnMenuItemClickListener { item ->
//            when (item.itemId) {
//                R.id.add_new_post -> goToActivity(ActivityNewPost::class.java)
//                R.id.switch_theme -> changeColorMode()
//            }
//            true
//        }
    }


    override fun initObservers() {

    }

    private fun changeColorMode() {
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            Configuration.UI_MODE_NIGHT_NO ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
}