package com.eccard.popularmovies.ui.main

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eccard.popularmovies.BR
import com.eccard.popularmovies.R
import com.eccard.popularmovies.data.network.model.Movie
import com.eccard.popularmovies.data.network.model.MovieOrderType
import com.eccard.popularmovies.databinding.ActivityMainBinding
import com.eccard.popularmovies.ui.base.BaseActivity
import com.eccard.popularmovies.ui.moviedetail.MovieDetailActivity
import com.eccard.popularmovies.utils.AppExecutors
import com.eccard.popularmovies.utils.ItemOffsetDecoration
import com.eccard.popularmovies.utils.RetryCallback
import com.google.android.material.snackbar.Snackbar
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject
import kotlin.math.roundToInt

class MainActivity : BaseActivity<ActivityMainBinding>(), LifecycleOwner, HasSupportFragmentInjector {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    lateinit var adapter : MovieAdapter
    private lateinit var mActivityMainBinding: ActivityMainBinding

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var appExecutors: AppExecutors

    val mainViewModel: MainViewModel by viewModels {
        viewModelFactory
    }

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

        mainViewModel.setNewOrder(MovieOrderType.POPULAR)

    }

    private fun setupBottomNavigation() {
        mActivityMainBinding.navigation.setOnNavigationItemSelectedListener { p0 ->
            val newOrderType: MovieOrderType = when (p0.itemId){
                R.id.nav_most_popular -> MovieOrderType.POPULAR
                R.id.nav_top_rated -> MovieOrderType.TOP_RATED
                else -> MovieOrderType.TOP_BOOKMARK
            }

            if (mainViewModel.orderType.value === newOrderType) {
                getViewDataBinding().rvMovies.post {
                    getViewDataBinding().rvMovies.smoothScrollToPosition(0)
                }
            } else {
                mainViewModel.setNewOrder(newOrderType)
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
                if (mainViewModel.orderType.value != MovieOrderType.TOP_BOOKMARK ){
                    val gridLayoutManager = recyclerView.layoutManager as GridLayoutManager
                    val lastPosition = gridLayoutManager.findLastVisibleItemPosition()
                    if (rvAdapter.itemCount > 0 ) {
                        if (lastPosition == rvAdapter.itemCount - 1) {
                            mainViewModel.loadNextPage()
                        }
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

    override fun getBindingVariable(): Int = BR.viewModel


    private fun onSelectedMovie(movie: Movie) {
        val intent = MovieDetailActivity.newIntent(this@MainActivity)
        intent.putExtra(Movie::class.java.simpleName, movie)

        startActivity(intent)
    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector
}
