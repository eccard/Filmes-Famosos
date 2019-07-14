package com.eccard.popularmovies.ui.main

import com.eccard.popularmovies.data.network.model.MovieResult
import muxi.kotlin.walletfda.ui.base.BaseView

interface MainNavigator : BaseView {
    fun onSelectedMovie(movie: MovieResult)
    fun showLoading()
    fun hideLoading()
    fun onMovieError(throwable: Throwable)
}