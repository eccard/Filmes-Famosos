package com.example.eccard.filmesfamosos.ui.moviedetail.reviews

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.eccard.filmesfamosos.R
import com.example.eccard.filmesfamosos.data.network.api.AppApiHelper
import com.example.eccard.filmesfamosos.data.network.model.MovieResult
import com.example.eccard.filmesfamosos.data.network.model.MovieReviewResponse
import com.example.eccard.filmesfamosos.data.network.model.MovieReviewResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import muxi.kotlin.walletfda.ui.base.BaseViewModel
import javax.inject.Inject

class ReviewsViewModel @Inject constructor(private var apiHelper: AppApiHelper):
        BaseViewModel<Any>() {

    companion object {
        val TAG = ReviewsViewModel::class.java.simpleName
    }


    var movie = MutableLiveData<MovieResult>()

    var review = MutableLiveData<List<MovieReviewResult>>()

    private var reviewDataRepo: MutableList<MovieReviewResult> = mutableListOf()

    fun getReviewAt(index: Int?): MovieReviewResult? {
        return if (index != null && reviewDataRepo.size > index) {
            reviewDataRepo[index]
        } else null
    }

    private var reviewsAdapter: ReviewsAdapter

    fun getAdapter() = reviewsAdapter

    init {
        reviewsAdapter = ReviewsAdapter(R.layout.adapter_review_item,this)
        getReviews(1)
    }


    fun getReviews(page : Int){

        val scope = CoroutineScope(Dispatchers.Main)

        scope.launch(context = Dispatchers.Main) {

            val response = withContext(context = Dispatchers.IO) {
                apiHelper.doGetReviewsFromMovieApiCall(movie.value!!.id,page)
            }

            if (response.isSuccessful){
                review.value = (response.body() as MovieReviewResponse).results
            } else {
                Log.e(TAG,"Error loading reviews")
            }
        }
    }


    fun updateTralersList(reviews: List<MovieReviewResult>) {
        reviewDataRepo.addAll(reviews)

        reviewsAdapter.setReviews(reviewDataRepo)
        reviewsAdapter.notifyDataSetChanged()
    }
}