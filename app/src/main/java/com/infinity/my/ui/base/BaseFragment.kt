package com.infinity.my.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.infinity.my.utils.hideKeyboard

abstract class BaseFragment<ViewBinding : ViewDataBinding>(
    @LayoutRes private val layout: Int,
    @IdRes private val screenTitle: Int = 0
) : Fragment(), IBaseView, LifecycleObserver {

    protected lateinit var binding: ViewBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layout, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        afterCreateView()
        initObservers()
    }

    override fun onDestroyView() {
        binding.root.hideKeyboard()
        binding.unbind()
        super.onDestroyView()
    }

    infix fun <T> LiveData<T>.observe(observer: Observer<T>) = observe(viewLifecycleOwner, observer)

}