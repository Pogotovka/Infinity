package com.infinity.my.ui.activitymain.fragmentlistpost

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.infinity.my.R
import com.infinity.my.data.model.db.PostDB
import com.infinity.my.databinding.ItemPostFailedBinding
import com.infinity.my.databinding.ItemPostUploadedBinding
import org.koin.dsl.module

val postAdapterModule = module {
    single { PostAdapter() }
}

class PostAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var callbackPostAdapter: CallbackPostAdapter? = null
    private var mData: List<PostDB> = emptyList()

    interface CallbackPostAdapter {
        fun onRetryClicked(post: PostDB)
    }

    fun setOnCallbackPostAdapterListener(callbackPostAdapter: CallbackPostAdapter) {
        this.callbackPostAdapter = callbackPostAdapter
    }

    fun swapData(list: List<PostDB>) {
        DiffUtil.calculateDiff(DiffUtilCallback(mData, list)).apply {
            mData = list
        }.dispatchUpdatesTo(this)
    }

    override fun getItemViewType(position: Int): Int =
        when (this.mData[position].isUploaded) {
            true -> ITEM_UPLOADED
            false -> ITEM_FAILED
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
        ITEM_UPLOADED -> PostHolderUploaded(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_post_uploaded, parent, false))
        ITEM_FAILED -> PostHolderFailed(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_post_failed, parent, false))
        else -> PostHolderUploaded(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_post_uploaded, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val post = mData[position]
        when (holder) {
            is PostHolderUploaded -> {
                holder.binding.post = post

            }
            is PostHolderFailed -> {
                holder.binding.post = post
                holder.binding.retry.setOnClickListener { callbackPostAdapter?.onRetryClicked(post) }
                setupProgress(holder.binding, post)
            }
        }
    }

    private fun setupProgress(binding: ItemPostFailedBinding, post: PostDB) {
//        println(post.progressUpload)
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
//            binding.linearProgressIndicator.setProgress(post.progressUpload, true)
//        else binding.linearProgressIndicator.progress = post.progressUpload
    }

    override fun getItemCount(): Int = this.mData.size

    inner class PostHolderUploaded(itemView: ItemPostUploadedBinding) : RecyclerView.ViewHolder(itemView.root) {
        val binding = itemView
    }


    inner class PostHolderFailed(itemView: ItemPostFailedBinding) : RecyclerView.ViewHolder(itemView.root) {
        val binding = itemView
    }

    companion object {
        const val ITEM_UPLOADED = 1
        const val ITEM_FAILED = 2
    }

    private inner class DiffUtilCallback(
        val oldList: List<PostDB>,
        val newList: List<PostDB>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldConcert = oldList[oldItemPosition]
            val newConcert = newList[newItemPosition]
            return oldConcert.id == newConcert.id && oldConcert.isUploaded == newConcert.isUploaded
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldConcert = oldList[oldItemPosition]
            val newConcert = newList[newItemPosition]
            return oldConcert.hashCode() == newConcert.hashCode()
        }
    }
}