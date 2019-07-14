package com.eccard.filmesfamosos.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.eccard.filmesfamosos.data.network.model.MovieResult

class MoviesAdapter (@param:LayoutRes private val layoutId: Int, private val viewModel: MainViewModel) :
        RecyclerView.Adapter<MovieViewHolder>(){

    private var movies = mutableListOf<MovieResult>()

    fun setMovies(movieResult: MutableList<MovieResult>){
        this.movies = movieResult
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int) = layoutId

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =  DataBindingUtil.inflate<ViewDataBinding>(layoutInflater,viewType,parent,false)
        return MovieViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return  movies.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(viewModel,position)
    }
}