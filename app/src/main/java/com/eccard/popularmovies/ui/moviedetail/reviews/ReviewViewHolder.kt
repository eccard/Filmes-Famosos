package com.eccard.popularmovies.ui.moviedetail.reviews

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.eccard.popularmovies.BR

class ReviewViewHolder (val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root){

    fun bind(viewModel: ReviewsViewModel, position: Int?){
        binding.setVariable(BR.viewModel, viewModel)
        binding.setVariable(BR.position, position)
        binding.executePendingBindings()
    }
}