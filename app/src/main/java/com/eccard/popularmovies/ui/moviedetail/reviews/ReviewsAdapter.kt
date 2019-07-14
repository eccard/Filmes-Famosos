package com.eccard.filmesfamosos.ui.moviedetail.reviews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.eccard.filmesfamosos.data.network.model.MovieReviewResult

class ReviewsAdapter (@param:LayoutRes private val layoutId: Int, private val viewModel: ReviewsViewModel):
        RecyclerView.Adapter<ReviewViewHolder>(){

    private var reviews = listOf<MovieReviewResult>()

    fun setReviews(reviews : List<MovieReviewResult>){
        this.reviews = reviews
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int) = layoutId

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater,viewType,parent,false)

        return ReviewViewHolder(binding)
    }

    override fun getItemCount() = reviews.size

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(viewModel,position)
    }
}