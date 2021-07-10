package com.infinity.my.ui.newpost.fragmentpostdescription

import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.infinity.my.R
import com.infinity.my.databinding.FragmentPostDescriptionBinding
import com.infinity.my.ui.base.BaseFragment
import com.infinity.my.utils.Constants.DEBOUNCE
import com.infinity.my.utils.SingleEvent
import com.infinity.my.utils.onClicked
import com.infinity.my.utils.showSnackbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.viewmodel.ext.android.viewModel

class FragmentPostDescription : BaseFragment<FragmentPostDescriptionBinding>(R.layout.fragment_post_description) {

    private val viewModel: PostDescriptionViewModel by viewModel()
    private val args: FragmentPostDescriptionArgs by navArgs()

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun afterCreateView() {
        binding.viewModel = viewModel
        args.imageUrl?.let { viewModel.swapData(it) }

        binding.publishPost
            .onClicked()
            .debounce(DEBOUNCE)
            .filter { viewModel.isValidData() }
            .onEach { viewModel.savePost() }
            .launchIn(lifecycleScope)
    }

    override fun initObservers() {
        showSnackbar(viewModel.showMessage)

        viewModel.postSaved.onEach {
            requireActivity().finish()
        }.launchIn(lifecycleScope)
    }

    private fun showSnackbar(singleEvent: LiveData<SingleEvent<Any>>) {
        binding.root.showSnackbar(this, singleEvent, Snackbar.LENGTH_LONG)
    }
}