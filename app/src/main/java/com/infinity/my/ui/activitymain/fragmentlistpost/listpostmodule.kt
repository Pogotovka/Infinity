package com.infinity.my.ui.activitymain.fragmentlistpost

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val listPostModule = module {
    viewModel { FragmentListPostViewModel(get(), get(), get()) }
}