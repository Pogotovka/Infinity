package com.infinity.my.ui.activitymain.fragmentlistpost

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.infinity.my.R
import com.infinity.my.data.model.db.PostDB
import com.infinity.my.databinding.FragmentListPostBinding
import com.infinity.my.ui.base.BaseFragment
import com.infinity.my.utils.SingleEvent
import com.infinity.my.utils.showSnackbar
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class FragmentListPost : BaseFragment<FragmentListPostBinding>(R.layout.fragment_list_post),
    SwipeRefreshLayout.OnRefreshListener, PostAdapter.CallbackPostAdapter {

    private val viewModel: FragmentListPostViewModel by viewModel()
    private val postAdapter: PostAdapter by inject()

    override fun afterCreateView() {
        binding.viewModel = viewModel

        viewModel.getAllPost()

        binding.rvLocations.adapter = postAdapter
        postAdapter.setOnCallbackPostAdapterListener(this)
        binding.swipeRefreshLayout.setOnRefreshListener(this)
    }

    override fun initObservers() {
        lifecycleScope.launch {
            viewModel.postsLiveData
                .flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
                .collect(::handlePostList)

            viewModel.progressUploadAttachment
                .flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
                .collect(::progressUploadAttachment)

            viewModel.uploadedPost
                .flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
                .collect(::postIsUploaded)
        }

        showSnackbar(viewModel.showMessage)
        binding.root.showSnackbar(this, viewModel.showMessage, Snackbar.LENGTH_LONG)
    }

    private fun postIsUploaded(post: PostDB?) {
        viewModel.getAllPost()
    }

    @FlowPreview
    override fun onRetryClicked(post: PostDB) {
        viewModel.uploadFailedPost(post)
    }

    private fun progressUploadAttachment(progress: Int) {
        println(progress)
        //        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
        //            binding.linearProgressIndicator.setProgress(progress, true)
        //        else binding.linearProgressIndicator.progress = progress
    }

    private fun handlePostList(result: List<PostDB>) {
        println(result.size)
        binding.swipeRefreshLayout.isRefreshing = false
        postAdapter.swapData(result)
    }

    private fun showSnackbar(singleEvent: LiveData<SingleEvent<Any>>) {
        binding.swipeRefreshLayout.isRefreshing = false
        binding.root.showSnackbar(this, singleEvent, Snackbar.LENGTH_LONG)
    }

    override fun onRefresh() {
        binding.swipeRefreshLayout.isRefreshing = true
        viewModel.getAllPost()
    }
}