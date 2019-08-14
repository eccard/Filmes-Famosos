package com.eccard.popularmovies.ui.moviedetail.reviews

import android.util.Log
import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import com.eccard.popularmovies.R
import com.eccard.popularmovies.data.network.api.AppApiHelper
import com.eccard.popularmovies.data.network.model.MovieResult
import com.eccard.popularmovies.data.network.model.network.MovieReviewResponse
import com.eccard.popularmovies.data.network.model.MovieReviewResult
import com.eccard.popularmovies.ui.moviedetail.trailers.TrailerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.eccard.popularmovies.ui.base.BaseViewModel
import javax.inject.Inject

class ReviewsViewModel @Inject constructor(private var apiHelper: AppApiHelper):
        BaseViewModel<Any>() {

    companion object {
        val TAG = ReviewsViewModel::class.java.simpleName
    }


    var movie = MutableLiveData<MovieResult>()

    var showEmpty = ObservableInt(View.GONE)
    var loading = ObservableInt(View.VISIBLE)
    var showError = ObservableInt(View.INVISIBLE)
    var showErrorGeneric = ObservableInt(View.INVISIBLE)


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
        loading.set(View.VISIBLE)
        scope.launch(context = Dispatchers.Main) {

            try {
                val response = withContext(context = Dispatchers.IO) {
                    apiHelper.doGetReviewsFromMovieApiCall(movie.value!!.id, page)
                }
                if (response.isSuccessful) {
                    showError.set(View.INVISIBLE)
                    review.value = (response.body() as MovieReviewResponse).results
                } else {
                    Log.e(TAG, "Error loading reviews")
                    showError.set(View.VISIBLE)
                }
            } catch (e: Exception){
                Log.e(TrailerViewModel.TAG, "Error loading reviews")
                if(reviewDataRepo.size == 0){
                    showError.set(View.VISIBLE)
                    showErrorGeneric.set(View.VISIBLE)
                    showEmpty.set(View.INVISIBLE)
                } else {
                    showError.set(View.INVISIBLE)
                }

            } finally {
                loading.set(View.INVISIBLE)
            }
        }
    }


    fun updateReviewList(reviews: List<MovieReviewResult>) {
        reviewDataRepo.addAll(reviews)

        if (reviewDataRepo.size == 0){
            showError.set(View.VISIBLE)
            showErrorGeneric.set(View.INVISIBLE)
            showEmpty.set(View.VISIBLE)
        } else {
            showError.set(View.INVISIBLE)
        }

        reviewsAdapter.setReviews(reviewDataRepo)
        reviewsAdapter.notifyDataSetChanged()
    }
}