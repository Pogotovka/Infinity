package com.infinity.my.utils

import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.infinity.my.R
import com.infinity.my.data.model.ImagePath
import com.infinity.my.ui.customui.RadiusButtonView

object BindingAdaptersManager {

    @JvmStatic
    @BindingAdapter("imageFromUrlGallery")
    fun bindImageFromUrlGallery(view: AppCompatImageView, imagePath: ImagePath?) {
        Glide.with(view.context)
            .load(imagePath)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .transform(MultiTransformation(CenterCrop(), RoundedCorners(view.resources.getDimensionPixelSize(R.dimen.galleryItemRoundSize))))
            .placeholder(R.drawable.ic_placeholder_image)
            .into(view); }

    @JvmStatic
    @BindingAdapter("imageFromUrl")
    fun bindImageFromUrl(view: AppCompatImageView, imagePath: ImagePath?) {
        Glide.with(view.context)
            .load(imagePath)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(view)
    }

    @BindingAdapter("bg_color")
    fun bgColor(view: RadiusButtonView, color: Int) {
        view.setBgColor(color)
    }
}