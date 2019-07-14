package com.example.eccard.filmesfamosos.ui.main

import com.example.eccard.filmesfamosos.data.network.model.MovieResult
import muxi.kotlin.walletfda.ui.base.BaseView

interface MainNavigator : BaseView {
    fun onSelectedMovie(movie: MovieResult)
    fun showLoading()
    fun hideLoading()
    fun onMovieError(throwable: Throwable)
}