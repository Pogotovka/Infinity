package com.infinity.my.ui.newpost.fragmentchooseimage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.infinity.my.R
import com.infinity.my.data.model.dto.GalleryFolder
import com.infinity.my.databinding.ListItemGalleryAlbumBinding
import com.infinity.my.utils.castTo
import org.koin.dsl.module

val folderAdapterModule = module {
    single { FolderAdapter() }
}

class FolderAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mData: List<GalleryFolder> = listOf()

    private var selectedItems: MutableList<Int> = mutableListOf()

    lateinit var callback: CallbackFolder

    fun initCallback(callback: CallbackFolder) {
        this.callback = callback
    }

    fun swapData(newData: List<GalleryFolder>) {
        DiffUtil.calculateDiff(DiffUtilCallback(mData, newData)).apply {
            mData = newData

        }.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        FolderViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.list_item_gallery_album, parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        mData[position].let { m ->
            holder.castTo<FolderViewHolder>().apply {
                binding.folder = m

                selectedItems.selectFirstIfNotChosen()

                binding.isSelected = selectedItems.contains(position)

                binding.root.setOnClickListener {
                    when {
                        selectedItems.size != 0 -> {
                            val oldSelected = selectedItems[0]
                            selectedItems.clear()
                            notifyItemChanged(oldSelected)
                        }
                    }
                    selectedItems.add(position)
                    notifyItemChanged(position)
                    callback.onFolderSelected(m)
                }
            }
        }
    }

    private fun MutableList<Int>.selectFirstIfNotChosen() {
        if (isNullOrEmpty() and (mData.isNotEmpty())) {
            add(0)
            callback.onFolderSelected(mData.first())
        }
    }

    override fun getItemCount(): Int = mData.size

    inner class FolderViewHolder(view: ListItemGalleryAlbumBinding) :
        RecyclerView.ViewHolder(view.root) {
        val binding = view
    }

    interface CallbackFolder {
        fun onFolderSelected(data: GalleryFolder)
    }

    private inner class DiffUtilCallback(
        val oldList: List<GalleryFolder>,
        val newList: List<GalleryFolder>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldConcert = oldList[oldItemPosition]
            val newConcert = newList[newItemPosition]
            return oldConcert.folderName == newConcert.folderName
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldConcert = oldList[oldItemPosition]
            val newConcert = newList[newItemPosition]
            return oldConcert.hashCode() == newConcert.hashCode()
        }
    }
}