package com.infinity.my.ui.newpost.fragmentchooseimage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.infinity.my.R
import com.infinity.my.data.model.dto.MediaItem
import com.infinity.my.databinding.ListItemGalleryBinding
import com.infinity.my.utils.castTo
import org.koin.dsl.module

val galleryAdapterModule = module {
    single { GalleryAdapter() }
}

class GalleryAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mData: List<MediaItem> = listOf()

    private var selectedItems: MutableList<Int> = mutableListOf()

    lateinit var callback: CallbackGallery

    fun initCallback(callback: CallbackGallery) {
        this.callback = callback
    }

    fun swapData(newData: List<MediaItem>) {
        DiffUtil.calculateDiff(DiffUtilCallback(mData, newData)).apply {
            mData = newData

        }.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        GalleryViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.list_item_gallery, parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        mData[position].let { m ->
            holder.castTo<GalleryViewHolder>().apply {
                binding.media = m

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
                    callback.onImageSelected(m)
                }
            }
        }
    }

    override fun getItemCount(): Int = mData.size

    inner class GalleryViewHolder(view: ListItemGalleryBinding) :
        RecyclerView.ViewHolder(view.root) {
        val binding = view
    }

    interface CallbackGallery {
        fun onImageSelected(m: MediaItem)
    }

    private inner class DiffUtilCallback(
        val oldList: List<MediaItem>,
        val newList: List<MediaItem>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldConcert = oldList[oldItemPosition]
            val newConcert = newList[newItemPosition]
            return oldConcert.thumb == newConcert.thumb
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldConcert = oldList[oldItemPosition]
            val newConcert = newList[newItemPosition]
            return oldConcert.hashCode() == newConcert.hashCode()
        }
    }

}