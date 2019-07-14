package com.eccard.popularmovies.ui.main

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

object CustomViewBindings {

    @BindingAdapter("imageUrl")
    @JvmStatic
    fun bindRecyclerViewAdapter(imageView: ImageView?, imageUrl: String?) {
        if (imageUrl != null) {
            val picasso: Picasso = Picasso.get()
            picasso.setIndicatorsEnabled(true)
            picasso.load(imageUrl)
                    .fit()
                    .centerCrop()
                    .into(imageView)
        } else {
            imageView?.setImageBitmap(null)
        }
    }
}