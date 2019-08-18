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

package com.eccard.popularmovies.ui.moviedetail.trailers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.eccard.popularmovies.utils.AppExecutors
import com.eccard.popularmovies.R
import com.eccard.popularmovies.data.network.model.MovieTrailer
import com.eccard.popularmovies.databinding.AdapterTrailerItemBinding
import com.eccard.popularmovies.utils.rv.DataBoundListAdapter

class MovieTrailerAdapter (
//        private val dataBindingComponent: DataBindingComponent,
        appExecutors: AppExecutors,
        private val repoClickCallback: ((MovieTrailer) -> Unit)?
) : DataBoundListAdapter<MovieTrailer, AdapterTrailerItemBinding>(
        appExecutors = appExecutors,
        diffCallback = object : DiffUtil.ItemCallback<MovieTrailer>() {
            override fun areItemsTheSame(oldItem: MovieTrailer, newItem: MovieTrailer): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MovieTrailer, newItem: MovieTrailer): Boolean {
                return oldItem.key == newItem.key
                        && oldItem.name == newItem.name
            }
        }
) {
    override fun createBinding(parent: ViewGroup): AdapterTrailerItemBinding {
        val binding = DataBindingUtil.inflate<AdapterTrailerItemBinding>(
                LayoutInflater.from(parent.context),
                R.layout.adapter_trailer_item,
                parent,
                false
//                ,dataBindingComponent
        )

        binding.root.setOnClickListener {
            binding.movieTrailer?.let {
                repoClickCallback?.invoke(it)
            }
        }
        return binding
    }

    override fun bind(binding: AdapterTrailerItemBinding, item: MovieTrailer) {
        binding.movieTrailer = item
    }
}