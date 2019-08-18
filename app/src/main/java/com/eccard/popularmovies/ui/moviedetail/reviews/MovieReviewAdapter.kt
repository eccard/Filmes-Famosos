package com.eccard.popularmovies.ui.moviedetail.reviews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.eccard.popularmovies.utils.AppExecutors
import com.eccard.popularmovies.R
import com.eccard.popularmovies.data.network.model.MovieReview
import com.eccard.popularmovies.databinding.AdapterReviewItemBinding
import com.eccard.popularmovies.utils.rv.DataBoundListAdapter

class MovieReviewAdapter(//        private val dataBindingComponent: DataBindingComponent,
        appExecutors: AppExecutors
) : DataBoundListAdapter<MovieReview, AdapterReviewItemBinding>(
        appExecutors = appExecutors,
        diffCallback = object : DiffUtil.ItemCallback<MovieReview>(){
            override fun areItemsTheSame(oldItem: MovieReview, newItem: MovieReview): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MovieReview, newItem: MovieReview): Boolean {
                return oldItem.author == newItem.author
                        && oldItem.content == newItem.content
            }
        }
){

    override fun createBinding(parent: ViewGroup): AdapterReviewItemBinding {
        val binding = DataBindingUtil.inflate<AdapterReviewItemBinding>(
                LayoutInflater.from(parent.context),
                R.layout.adapter_review_item,
                parent,
                false
//                ,dataBindingComponent
        )

        return binding
    }

    override fun bind(binding: AdapterReviewItemBinding, item: MovieReview) {
        binding.movieReview = item
    }
}