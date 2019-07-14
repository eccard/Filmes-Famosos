package com.eccard.popularmovies.ui.main

import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.databinding.ViewDataBinding
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.eccard.popularmovies.BR
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.movie_item_view_holder.view.*


class MovieViewHolder (val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root){

    fun bind(viewModel: MainViewModel,position: Int?){
        binding.setVariable(BR.viewModel, viewModel)
        binding.setVariable(BR.position, position)
        binding.executePendingBindings()
        binding.root.img_movie_item

        val picasso: Picasso = Picasso.get()
        picasso.setIndicatorsEnabled(true)
        picasso.load(viewModel.getMovieAt(position)!!.generatePosterUrl())
                .fit()
                .centerCrop()
                .into(binding.root.img_movie_item, object : Callback {
                    override fun onSuccess() {

                        val bitmap = (binding.root.img_movie_item.getDrawable() as BitmapDrawable).bitmap

                        Palette.from(bitmap).generate { palette ->
                            setUpInfoBackgroundColor(binding,palette!!)
                        }
                    }

                    override fun onError(e: Exception?) {
                        Log.e("MovieViewHolder",Log.getStackTraceString(e))
                    }
                })
    }

    private fun setUpInfoBackgroundColor(binding: ViewDataBinding,palette: Palette) {

        var swatch = palette.getVibrantSwatch()
        //If there is no vibrant swatch, try getting dominant swatch
        if(swatch == null) {
            swatch = palette.getDominantSwatch();
        }

        // If there is no dominant swatch try getting muted swatch
        if(swatch == null) {
            swatch = palette.getMutedSwatch();
        }
        // If there is no dominant swatch try getting muted swatch
        swatch?.let {
            binding.root.image_legend.setBackgroundColor(it.getRgb())
            binding.root.tv_movie_title.setTextColor(it.getBodyTextColor());
            binding.root.tv_movie_rate.setTextColor(it.getTitleTextColor())
        }

    }
}