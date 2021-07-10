package com.infinity.my.ui.newpost.fragmentchooseimage

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val chooseImageModule = module {
    viewModel { ChooseImageViewModel(get()) }
}