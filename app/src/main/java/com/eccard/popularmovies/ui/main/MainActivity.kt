package com.eccard.popularmovies.ui.main

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eccard.popularmovies.AppExecutors
import com.eccard.popularmovies.BR
import com.eccard.popularmovies.R
import com.eccard.popularmovies.data.network.api.AppApiHelper
import com.eccard.popularmovies.data.network.model.MovieResult
import com.eccard.popularmovies.databinding.ActivityMainBinding
import com.eccard.popularmovies.di.ViewModelProviderFactory
import com.eccard.popularmovies.utils.RetryCallback
import com.eccard.popularmovies.ui.moviedetail.MovieDetailActivity
import com.eccard.popularmovies.utils.ItemOffsetDecoration
import com.google.android.material.snackbar.Snackbar
import com.eccard.popularmovies.ui.base.BaseActivity
import javax.inject.Inject
import kotlin.math.roundToInt

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(), LifecycleOwner {

    lateinit var adapter : MovieAdapter
    private lateinit var mActivityMainBinding: ActivityMainBinding

    @Inject
    lateinit var factory: ViewModelProviderFactory

    @Inject
    lateinit var appExecutors: AppExecutors

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        mActivityMainBinding = getViewDataBinding()

        mActivityMainBinding.searchResult = mainViewModel.results

        setUpViews()

    }

    private fun setUpViews() {

        setupRecyclerView()

        setupBottomNavigation()

        mainViewModel.getDataBaseMovies().observe(this, Observer {
            mainViewModel.moviesFromDb = it
            Log.d("MainActivity","update moview from db" + it.toString())
        })

        mainViewModel.setNewOrder(AppApiHelper.MovieOrderType.POPULAR)

    }

    private fun setupBottomNavigation() {
        mActivityMainBinding.navigation.setOnNavigationItemSelectedListener { p0 ->
            val newOrderType: AppApiHelper.MovieOrderType = when (p0.itemId){
                R.id.nav_most_popular -> AppApiHelper.MovieOrderType.POPULAR
                R.id.nav_top_rated -> AppApiHelper.MovieOrderType.TOP_RATED
                else -> AppApiHelper.MovieOrderType.TOP_BOOKMARK
            }

            if (mainViewModel.mCurrentMovieOrderType === newOrderType) {
                getViewDataBinding().rvMovies.post {
                    getViewDataBinding().rvMovies.smoothScrollToPosition(0)
                }
            } else {
//                scrollListener!!.resetState()
                mainViewModel.mCurrentMovieOrderType = newOrderType

                mainViewModel.setNewOrder(newOrderType)
//                mainViewModel.getFirstPage()
            }
            true
        }
    }

    private fun setupRecyclerView(){
        val posterWidth = mActivityMainBinding.rvMovies.context.resources.getDimension(R.dimen.img_view_recycler_view_holder_width)
        val display = windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)
        val screenWidth = outMetrics.widthPixels.toFloat()
        val bestSpanCount = (screenWidth / posterWidth).roundToInt()

        mActivityMainBinding.rvMovies.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(mActivityMainBinding.rvMovies.context,bestSpanCount)
        mActivityMainBinding.rvMovies.layoutManager = layoutManager
        mActivityMainBinding.rvMovies.setHasFixedSize(true)


        val rvAdapter = MovieAdapter(appExecutors = appExecutors){
            movieResult ->  onSelectedMovie(movieResult)
        }
        mActivityMainBinding.rvMovies.adapter =  rvAdapter
        adapter = rvAdapter

        val itemOffsetDecoration = ItemOffsetDecoration(this@MainActivity,
                R.dimen.grid_spacing_small)
        mActivityMainBinding.rvMovies.addItemDecoration(itemOffsetDecoration)


        mActivityMainBinding.rvMovies.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val gridLayoutManager = recyclerView.layoutManager as GridLayoutManager
                val lastPosition = gridLayoutManager.findLastVisibleItemPosition()
                if (rvAdapter.itemCount > 0 ) {
                    if (lastPosition == rvAdapter.itemCount - 1) {
                        mainViewModel.loadNextPage()
                    }
                }
            }
        })

        mainViewModel.results.observe(this, Observer { result ->
            adapter.submitList(result?.data)
            mActivityMainBinding.invalidateAll()
        })

        mainViewModel.loadMoreStatus.observe(this, Observer { loadingMore ->
            if (loadingMore == null) {
                mActivityMainBinding.loadingMore = false
            } else {
                mActivityMainBinding.loadingMore = loadingMore.isRunning
                val error = loadingMore.errorMessageIfNotHandled
                if (error != null) {
                    Snackbar.make(mActivityMainBinding.loadMoreBar, error, Snackbar.LENGTH_LONG).show()
                }
            }
        })


        mActivityMainBinding.callback = object : RetryCallback {
            override fun retry() {
                mainViewModel.refresh()
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun getViewModel(): MainViewModel {
        mainViewModel = ViewModelProviders.of(this,factory)
                .get(MainViewModel::class.java)
        return mainViewModel
    }

    override fun getBindingVariable(): Int = BR.viewModel


    private fun onSelectedMovie(movie: MovieResult) {
        val intent = MovieDetailActivity.newIntent(this@MainActivity)
        intent.putExtra(MovieResult::class.java.simpleName, movie)

        startActivity(intent)
    }

}
