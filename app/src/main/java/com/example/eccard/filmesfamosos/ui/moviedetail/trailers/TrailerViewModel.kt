package com.example.eccard.filmesfamosos.ui.moviedetail.trailers

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.eccard.filmesfamosos.R
import com.example.eccard.filmesfamosos.data.network.api.AppApiHelper
import com.example.eccard.filmesfamosos.data.network.model.MovieResult
import com.example.eccard.filmesfamosos.data.network.model.MovieTrailersReviewResponse
import com.example.eccard.filmesfamosos.data.network.model.TrailerResult
import com.example.eccard.filmesfamosos.utils.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import muxi.kotlin.walletfda.ui.base.BaseViewModel
import javax.inject.Inject

class TrailerViewModel @Inject constructor(private var apiHelper: AppApiHelper):
    BaseViewModel<Any>(){

    companion object {

        val TAG = TrailerViewModel::class.java.simpleName
    }

    var movie = MutableLiveData<MovieResult>()
    var trailers = MutableLiveData<List<TrailerResult>>()
    private var trailersDataRepo : MutableList<TrailerResult> = mutableListOf()

    fun updateTralersList(trailers : List<TrailerResult>){
        trailersDataRepo.addAll(trailers)

        trailerAdapter.setTrailers(trailersDataRepo)
        trailerAdapter.notifyDataSetChanged()

    }

    private val _selected = MutableLiveData<Event<TrailerResult>>()

    val selected : LiveData<Event<TrailerResult>>
        get() = _selected


    fun getTrailerAt(index:Int?):TrailerResult?{
        return if (index != null && trailers.value!!.size > index){
            trailers.value!![index]
        } else null
    }


    private var trailerAdapter : TrailerAdapter
    init {
        trailerAdapter = TrailerAdapter(R.layout.adapter_trailer,this)
        getMovieTrailers()
    }

    fun getMovieTrailers(){

        val scope = CoroutineScope(Dispatchers.Main)

        scope.launch(context = Dispatchers.Main) {

            val response = withContext(context = Dispatchers.IO) {
                apiHelper.doGetTrailersFromMovieApiCall(movie.value!!.id,1)
            }

            if (response.isSuccessful){
                trailers.value = (response.body() as MovieTrailersReviewResponse).results
            } else {
                Log.e(TAG,"Error loading trailers")
            }

        }
    }


    fun onItemClick(index: Int?) {
        getTrailerAt(index)?.let {
            _selected.value = Event(it)
        }
    }

    fun getAdapter() = trailerAdapter
}