package com.eccard.popularmovies.ui.moviedetail.trailers

import android.util.Log
import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.eccard.popularmovies.R
import com.eccard.popularmovies.data.network.api.AppApiHelper
import com.eccard.popularmovies.data.network.model.MovieResult
import com.eccard.popularmovies.data.network.model.network.MovieTrailersReviewResponse
import com.eccard.popularmovies.data.network.model.TrailerResult
import com.eccard.popularmovies.utils.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.eccard.popularmovies.ui.base.BaseViewModel
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

    var showEmpty = ObservableInt(View.GONE)
    var loading = ObservableInt(View.VISIBLE)

    fun getTrailerAt(index:Int?):TrailerResult?{
        return if (index != null && trailersDataRepo.size > index){
            trailersDataRepo[index]
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

            loading.set(View.VISIBLE)
            showEmpty.set(View.INVISIBLE)
            try {
                val response = withContext(context = Dispatchers.IO) {
                    apiHelper.doGetTrailersFromMovieApiCall(movie.value!!.id)
                }

                if (response.isSuccessful) {
                    trailers.value = (response.body() as MovieTrailersReviewResponse).results
                } else {
                    Log.e(TAG, "Error loading trailers")
                }
            } catch (e: Exception){
                Log.e(TAG, "Error loading trailers")
                showEmpty.set(View.VISIBLE)
            } finally {
                loading.set(View.INVISIBLE)
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