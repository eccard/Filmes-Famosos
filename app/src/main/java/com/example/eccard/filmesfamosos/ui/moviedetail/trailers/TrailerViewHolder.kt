package com.example.eccard.filmesfamosos.ui.moviedetail.trailers

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.eccard.filmesfamosos.BR

class TrailerViewHolder  (val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(viewModel: TrailerViewModel, position: Int?){
        binding.setVariable(BR.viewModel, viewModel)
        binding.setVariable(BR.position, position)
        binding.executePendingBindings()
    }
}