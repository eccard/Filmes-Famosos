package com.example.eccard.filmesfamosos.ui.moviedetail.summary

import androidx.lifecycle.MutableLiveData
import com.example.eccard.filmesfamosos.data.network.api.AppApiHelper
import com.example.eccard.filmesfamosos.data.network.database.MovieDao
import com.example.eccard.filmesfamosos.data.network.model.MovieResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import muxi.kotlin.walletfda.ui.base.BaseViewModel
import javax.inject.Inject

class SummaryViewModel @Inject constructor(private var moviesDao: MovieDao, private var apiHelper: AppApiHelper):
        BaseViewModel<Any>(){

    var movie = MutableLiveData<MovieResult>()
    var movieIsBookmarked = MutableLiveData<Boolean>().apply { value = false }

    fun getMovieFromDb(id :Int) = moviesDao.getMovieResult(id)


    fun changeBookMarkState(){
        val scope = CoroutineScope(Dispatchers.Main)

        if ( movieIsBookmarked.value == false) {
            scope.launch(context = Dispatchers.Main) {
                withContext(context = Dispatchers.IO) {
                    moviesDao.insertMovie(movie.value!!)
                }
                movieIsBookmarked.value = true
            }

        } else {

            scope.launch(context = Dispatchers.Main) {
                withContext(context = Dispatchers.IO) {
                    moviesDao.deleteMovie(movie.value!!)
                }
                movieIsBookmarked.value = false
            }


        }
    }
}