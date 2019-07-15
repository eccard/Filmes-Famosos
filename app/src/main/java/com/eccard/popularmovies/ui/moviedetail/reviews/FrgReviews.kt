package com.eccard.popularmovies.ui.moviedetail.reviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eccard.popularmovies.BR
import com.eccard.popularmovies.R
import com.eccard.popularmovies.data.network.model.MovieResult
import com.eccard.popularmovies.databinding.FrgReviewsBinding
import com.eccard.popularmovies.di.ViewModelProviderFactory
import com.eccard.popularmovies.utils.EndlessRecyclerViewScrollListener
import muxi.kotlin.walletfda.ui.base.BaseFragment
import javax.inject.Inject

class FrgReviews : BaseFragment<FrgReviewsBinding,ReviewsViewModel>() {

    private lateinit var reviewViewModel: ReviewsViewModel

    @Inject
    lateinit var factory: ViewModelProviderFactory

    private lateinit var frgReviewsBinding: FrgReviewsBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        setUpRecyclerView()

        val intent = activity!!.intent
        if (intent.hasExtra(MovieResult::class.java.simpleName)) {
            val movieResult = intent.getParcelableExtra<MovieResult>(MovieResult::class.java.simpleName)
            reviewViewModel.movie.value = movieResult
        }

        return view
    }

    private fun setUpRecyclerView(){

        frgReviewsBinding = getViewDataBinding()

        val layoutManager = LinearLayoutManager(context)
        frgReviewsBinding.rvReviews.layoutManager = layoutManager
        frgReviewsBinding.rvReviews.adapter = reviewViewModel.getAdapter()

        val dividerItemDecoration = DividerItemDecoration(frgReviewsBinding.rvReviews.context,
                DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.divider)!!)

        frgReviewsBinding.rvReviews.addItemDecoration(dividerItemDecoration)

        var scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            public override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                reviewViewModel.getReviews(page)
            }
        }

        frgReviewsBinding.rvReviews.addOnScrollListener(scrollListener)

        reviewViewModel.review.observe(this, Observer {
            reviewViewModel.updateReviewList(it)
        })
    }

    companion object {
        private val TAG = FrgReviews::class.java.simpleName
    }


    override fun getLayoutId() = R.layout.frg_reviews

    override fun getViewModel(): ReviewsViewModel {
        reviewViewModel = ViewModelProviders.of(this,factory)
                .get(ReviewsViewModel::class.java)
        return reviewViewModel
    }

    override fun getBindingVariable() = BR.viewModel

    override fun showToast(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}