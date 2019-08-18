

/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.eccard.popularmovies.ui.main

import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.palette.graphics.Palette
import com.eccard.popularmovies.utils.AppExecutors
import com.eccard.popularmovies.R
import com.eccard.popularmovies.data.network.model.Movie
import com.eccard.popularmovies.databinding.MovieItemBinding
import com.eccard.popularmovies.utils.rv.DataBoundListAdapter
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.movie_item.view.*


/**
 * A RecyclerView adapter for [Movie] class.
 */
class MovieAdapter(
//        private val dataBindingComponent: DataBindingComponent,
        appExecutors: AppExecutors,
        private val repoClickCallback: ((Movie) -> Unit)?
) : DataBoundListAdapter<Movie, MovieItemBinding>(
        appExecutors = appExecutors,
        diffCallback = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.title == newItem.title
                        && oldItem.original_title == newItem.original_title
            }
        }
) {

    override fun createBinding(parent: ViewGroup): MovieItemBinding {
        val binding = DataBindingUtil.inflate<MovieItemBinding>(
                LayoutInflater.from(parent.context),
                R.layout.movie_item,
                parent,
                false
//                ,dataBindingComponent
        )
        binding.root.setOnClickListener {
            binding.movie?.let {
                repoClickCallback?.invoke(it)
            }
        }
        return binding
    }

    override fun bind(binding: MovieItemBinding, item: Movie) {
        binding.movie = item

        setupImg(item, binding)
    }

    private fun setupImg(item: Movie, binding: MovieItemBinding) {
        val picasso: Picasso = Picasso.get()
        picasso.setIndicatorsEnabled(true)
        picasso.load(item.generatePosterUrl())
                .fit()
                .centerCrop()
                .into(binding.root.img_movie_item, object : Callback {
                    override fun onSuccess() {

                        val bitmap = (binding.root.img_movie_item.drawable as BitmapDrawable).bitmap

                        Palette.from(bitmap).generate { palette ->
                            setUpInfoBackgroundColor(binding, palette!!)
                        }
                    }

                    override fun onError(e: Exception?) {
                        Log.e("MovieAdapter", Log.getStackTraceString(e))
                    }
                })
    }

    private fun setUpInfoBackgroundColor(binding: ViewDataBinding, palette: Palette) {

        var swatch = palette.vibrantSwatch
        //If there is no vibrant swatch, try getting dominant swatch
        if(swatch == null) {
            swatch = palette.dominantSwatch
        }

        // If there is no dominant swatch try getting muted swatch
        if(swatch == null) {
            swatch = palette.mutedSwatch
        }
        // If there is no dominant swatch try getting muted swatch
        swatch?.let {
            binding.root.image_legend.setBackgroundColor(it.rgb)
            binding.root.tv_movie_title.setTextColor(it.bodyTextColor)
            binding.root.tv_movie_rate.setTextColor(it.titleTextColor)
        }

    }

}
