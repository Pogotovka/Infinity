package com.infinity.my.ui.newpost.fragmentpostdescription

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val postDescriptionModule = module {
    viewModel { PostDescriptionViewModel(get()) }
}