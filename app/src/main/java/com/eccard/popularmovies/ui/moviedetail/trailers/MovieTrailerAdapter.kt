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
import com.eccard.popularmovies.AppExecutors
import com.eccard.popularmovies.R
import com.eccard.popularmovies.data.network.model.MovieResult
import com.eccard.popularmovies.data.network.model.TrailerResult
import com.eccard.popularmovies.databinding.AdapterTrailerBinding
import com.eccard.popularmovies.databinding.MovieItemBinding
import com.eccard.popularmovies.utils.rv.DataBoundListAdapter

class MovieTrailerAdapter (
//        private val dataBindingComponent: DataBindingComponent,
        appExecutors: AppExecutors,
        private val repoClickCallback: ((TrailerResult) -> Unit)?
) : DataBoundListAdapter<TrailerResult, AdapterTrailerBinding>(
        appExecutors = appExecutors,
        diffCallback = object : DiffUtil.ItemCallback<TrailerResult>() {
            override fun areItemsTheSame(oldItem: TrailerResult, newItem: TrailerResult): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: TrailerResult, newItem: TrailerResult): Boolean {
                return oldItem.key == newItem.key
                        && oldItem.name == newItem.name
            }
        }
) {
    override fun createBinding(parent: ViewGroup): AdapterTrailerBinding {
        val binding = DataBindingUtil.inflate<AdapterTrailerBinding>(
                LayoutInflater.from(parent.context),
                R.layout.adapter_trailer,
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

    override fun bind(binding: AdapterTrailerBinding, item: TrailerResult) {
        binding.movieTrailer = item
    }
}