package com.eccard.popularmovies.ui.moviedetail.trailers

import android.content.Intent
import android.net.Uri
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
import com.eccard.popularmovies.utils.AppExecutors
import com.eccard.popularmovies.BR
import com.eccard.popularmovies.R
import com.eccard.popularmovies.data.network.model.Movie
import com.eccard.popularmovies.databinding.FrgTrailersBinding
import com.eccard.popularmovies.di.ViewModelProviderFactory
import com.eccard.popularmovies.ui.base.BaseFragment
import com.eccard.popularmovies.utils.RetryCallback
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class FrgTrailers : BaseFragment<FrgTrailersBinding,TrailerViewModel>() {

    lateinit var adapter : MovieTrailerAdapter
    private lateinit var trailersViewModel: TrailerViewModel

    @Inject
    lateinit var factory: ViewModelProviderFactory

    @Inject
    lateinit var appExecutors: AppExecutors

    private lateinit var frgTrailersBinding: FrgTrailersBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        frgTrailersBinding = getViewDataBinding()

        frgTrailersBinding.searchResult = trailersViewModel.results


        trailersViewModel.loadMoreStatus.observe(this, Observer { loadingMore ->
            if (loadingMore == null) {
                frgTrailersBinding.loadingMore = false
            } else {
                frgTrailersBinding.loadingMore = loadingMore.isRunning
                val error = loadingMore.errorMessageIfNotHandled
                if (error != null) {
                    Snackbar.make(frgTrailersBinding.loadMoreBar, error, Snackbar.LENGTH_LONG).show()
                }
            }
        })

        frgTrailersBinding.callback = object : RetryCallback {
            override fun retry() {
                trailersViewModel.refresh()
            }
        }

        setupRecyclerView()
        return view
    }


    fun setupRecyclerView(){



        val rvAdapter = MovieTrailerAdapter(appExecutors = appExecutors){
            movieTrailer ->  onSelectedTrailers(movieTrailer.key)
        }
        frgTrailersBinding.rvTrailers.adapter = rvAdapter
        adapter = rvAdapter

        val dividerItemDecoration = DividerItemDecoration(frgTrailersBinding.rvTrailers.context,
                DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.divider)!!)

        frgTrailersBinding.rvTrailers.addItemDecoration(dividerItemDecoration)

//        val itemOffsetDecoration = ItemOffsetDecoration(this@MainActivity,
//                R.dimen.grid_spacing_small)
//        mActivityMainBinding.rvMovies.addItemDecoration(itemOffsetDecoration)

        frgTrailersBinding.rvTrailers.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastPosition = linearLayoutManager.findLastVisibleItemPosition()

                if (rvAdapter.itemCount > 0 ) {
                    if (lastPosition == rvAdapter.itemCount - 1) {
                        trailersViewModel.loadNextPage()
                    }
                }
            }
        })


        trailersViewModel.results.observe(this, Observer { result ->
            adapter.submitList(result?.data)
            frgTrailersBinding.invalidateAll()
        })


        val intent = activity!!.intent
        if (intent.hasExtra(Movie::class.java.simpleName)) {
            val movieResult = intent.getParcelableExtra<Movie>(Movie::class.java.simpleName)

            trailersViewModel.setMovieId(movieResult.id)
        }


    }

    private fun onSelectedTrailers(videoKey: String) {
        val videoIntent = Intent()
        videoIntent.action = Intent.ACTION_VIEW
        videoIntent.data = buildVideoUri(videoKey)

        if (videoIntent.resolveActivity(activity!!.packageManager) != null) {
            startActivity(videoIntent)
        }
    }


    companion object {

        private val YOUTUBE_BASE_URL = "https://www.youtube.com/watch"
        private fun buildVideoUri(videoKey: String): Uri {
            return Uri.parse(YOUTUBE_BASE_URL)
                    .buildUpon()
                    .appendQueryParameter("v", videoKey)
                    .build()
        }
    }


    override fun getLayoutId() = R.layout.frg_trailers

    override fun getViewModel(): TrailerViewModel {
        trailersViewModel = ViewModelProviders.of(this,factory)
                .get(TrailerViewModel::class.java)
        return trailersViewModel
    }

    override fun getBindingVariable() = BR.viewModel

}