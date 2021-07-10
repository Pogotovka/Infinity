package com.infinity.my.ui.activitymain

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val activityViewModule = module {
    viewModel { ActivityMainViewModel() }
}