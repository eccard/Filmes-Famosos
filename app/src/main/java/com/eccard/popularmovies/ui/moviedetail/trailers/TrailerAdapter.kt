package com.eccard.filmesfamosos.ui.moviedetail.trailers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.eccard.filmesfamosos.data.network.model.TrailerResult

class TrailerAdapter (@param:LayoutRes private val layoutId: Int, private val viewModel: TrailerViewModel):
        RecyclerView.Adapter<TrailerViewHolder>(){

    private var trailers = listOf<TrailerResult>()

    fun setTrailers(trailers: List<TrailerResult>){
        this.trailers = trailers
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int) = layoutId

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater,viewType,parent,false)

        return TrailerViewHolder(binding)

    }

    override fun getItemCount() = trailers.size

    override fun onBindViewHolder(holder: TrailerViewHolder, position: Int) {
        holder.bind(viewModel,position)
    }
}