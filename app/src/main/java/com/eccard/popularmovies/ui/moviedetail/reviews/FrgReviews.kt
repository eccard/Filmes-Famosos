package com.eccard.popularmovies.ui.moviedetail.reviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eccard.popularmovies.R
import com.eccard.popularmovies.data.network.model.Movie
import com.eccard.popularmovies.databinding.FrgReviewsBinding
import com.eccard.popularmovies.ui.base.BaseFragment
import com.eccard.popularmovies.utils.AppExecutors
import com.eccard.popularmovies.utils.RetryCallback
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class FrgReviews : BaseFragment<FrgReviewsBinding>() {

    lateinit var adapter : MovieReviewAdapter

    @Inject
    lateinit var appExecutors: AppExecutors

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val reviewViewModel: ReviewsViewModel by viewModels {
        viewModelFactory
    }

    private lateinit var frgReviewsBinding: FrgReviewsBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        setUpRecyclerView()

        val intent = activity!!.intent
        if (intent.hasExtra(Movie::class.java.simpleName)) {
            val movieResult = intent.getParcelableExtra<Movie>(Movie::class.java.simpleName)
            reviewViewModel.setMovieId(movieResult.id)
        }

        return view
    }

    private fun setUpRecyclerView(){

        frgReviewsBinding = getViewDataBinding()

        val rvAdapter = MovieReviewAdapter(appExecutors = appExecutors)
        frgReviewsBinding.rvReviews.adapter = rvAdapter
        adapter = rvAdapter

        val dividerItemDecoration = DividerItemDecoration(frgReviewsBinding.rvReviews.context,
                DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.divider)!!)

        frgReviewsBinding.rvReviews.addItemDecoration(dividerItemDecoration)

        frgReviewsBinding.rvReviews.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastPosition = linearLayoutManager.findLastVisibleItemPosition()
                if (rvAdapter.itemCount > 0 ) {
                    if (lastPosition == rvAdapter.itemCount - 1) {
                        reviewViewModel.loadNextPage()
                    }
                }
            }
        })

        frgReviewsBinding.searchResult = reviewViewModel.results
        reviewViewModel.results.observe(this, Observer { result ->
            adapter.submitList(result?.data)
            frgReviewsBinding.invalidateAll()
        })

        reviewViewModel.loadMoreStatus.observe(this, Observer { loadingMore ->
            if (loadingMore == null) {
                frgReviewsBinding.loadingMore = false
            } else {
                frgReviewsBinding.loadingMore = loadingMore.isRunning
                val error = loadingMore.errorMessageIfNotHandled
                if (error != null) {
                    Snackbar.make(frgReviewsBinding.loadMoreBar, error, Snackbar.LENGTH_LONG).show()
                }
            }
        })

        frgReviewsBinding.callback = object : RetryCallback {
            override fun retry() {
                reviewViewModel.refresh()
            }
        }
    }

    companion object {
        private val TAG = FrgReviews::class.java.simpleName
    }


    override fun getLayoutId() = R.layout.frg_reviews

//    override fun getViewModel(): ReviewsViewModel {
//        reviewViewModel = ViewModelProviders.of(this,factory)
//                .get(ReviewsViewModel::class.java)
//        return reviewViewModel
//    }

//    override fun getBindingVariable() = BR.viewModel


}