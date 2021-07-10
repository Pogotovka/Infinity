package com.infinity.my.ui.newpost.fragmenttakepicture

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val takePictureModule  = module {
    viewModel { TakePictureViewModel() }
}