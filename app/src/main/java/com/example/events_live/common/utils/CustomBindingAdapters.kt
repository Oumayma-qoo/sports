package com.example.events_live.common.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.events_live.R

object CustomBindingAdapters {

    @BindingAdapter("imageUrl")
    @JvmStatic
    fun loadImage(imageView: ImageView, url: String?) {
        url?.let {
            Glide
                .with(imageView.context)
                .load(it)
                .placeholder(R.drawable.ic_home_logo_)
                .into(imageView)
        }

    }

    @BindingAdapter("imageAwayUrl")
    @JvmStatic
    fun loadAwayImage(imageView: ImageView, url: String?) {
        url?.let {
            Glide
                .with(imageView.context)
                .load(it)
                .placeholder(R.drawable.ic_away_logo)
                .into(imageView)
        }

    }
}