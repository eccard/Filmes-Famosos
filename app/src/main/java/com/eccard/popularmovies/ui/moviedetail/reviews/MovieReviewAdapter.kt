package com.eccard.popularmovies.ui.moviedetail.reviews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.eccard.popularmovies.AppExecutors
import com.eccard.popularmovies.R
import com.eccard.popularmovies.data.network.model.MovieReviewResult
import com.eccard.popularmovies.databinding.ReviewItemBinding
import com.eccard.popularmovies.utils.rv.DataBoundListAdapter

class MovieReviewAdapter(//        private val dataBindingComponent: DataBindingComponent,
        appExecutors: AppExecutors
) : DataBoundListAdapter<MovieReviewResult, ReviewItemBinding>(
        appExecutors = appExecutors,
        diffCallback = object : DiffUtil.ItemCallback<MovieReviewResult>(){
            override fun areItemsTheSame(oldItem: MovieReviewResult, newItem: MovieReviewResult): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MovieReviewResult, newItem: MovieReviewResult): Boolean {
                return oldItem.author == newItem.author
                        && oldItem.content == newItem.content
            }
        }
){

    override fun createBinding(parent: ViewGroup): ReviewItemBinding {
        val binding = DataBindingUtil.inflate<ReviewItemBinding>(
                LayoutInflater.from(parent.context),
                R.layout.review_item,
                parent,
                false
//                ,dataBindingComponent
        )

        return binding
    }

    override fun bind(binding: ReviewItemBinding, item: MovieReviewResult) {
        binding.movieReview = item
    }
}