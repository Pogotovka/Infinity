package com.infinity.my.ui.base

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.infinity.my.utils.hideKeyboard

abstract class BaseActivity<ViewBinding : ViewDataBinding>(
    @LayoutRes private val layout: Int,
    @IdRes private val screenTitle: Int = 0
) : AppCompatActivity(), IBaseView {

    protected lateinit var binding: ViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layout)
        afterCreateView()
        initObservers()
    }

    override fun onBackPressed() {
        binding.root.hideKeyboard()
        super.onBackPressed()
    }

    override fun onDestroy() {
        if (::binding.isInitialized) binding.unbind()
        super.onDestroy()
    }
}