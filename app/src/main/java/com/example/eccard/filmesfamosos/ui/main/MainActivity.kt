package com.example.eccard.filmesfamosos.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.eccard.filmesfamosos.BR
import com.example.eccard.filmesfamosos.R
import com.example.eccard.filmesfamosos.data.network.api.AppApiHelper
import com.example.eccard.filmesfamosos.data.network.model.MovieResult
import com.example.eccard.filmesfamosos.databinding.ActivityMainBinding
import com.example.eccard.filmesfamosos.di.ViewModelProviderFactory
import com.example.eccard.filmesfamosos.ui.moviedetail.MovieDetailActivity
import com.example.eccard.filmesfamosos.utils.EndlessRecyclerViewScrollListener
//import kotlinx.android.synthetic.main.activity_main.*
import muxi.kotlin.walletfda.ui.base.BaseActivity
import javax.inject.Inject
//import android.R



class MainActivity : BaseActivity<ActivityMainBinding,MainViewModel>(), LifecycleOwner,MainNavigator {

    private var moviesAdapter: MoviesAdapter? = null
    private var scrollListener: EndlessRecyclerViewScrollListener? = null
    private var mCurrentMovieOrderType: AppApiHelper.MovieOrderType = AppApiHelper.MovieOrderType.POPULAR

//    private var mGetMovieFrg: GetMoviesFragment? = null


    private lateinit var mActivityMainBinding: ActivityMainBinding

    @Inject
    lateinit var factory: ViewModelProviderFactory

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mActivityMainBinding = getViewDataBinding()
        mainViewModel.setNavigator(this)

        setUpViews()

//        setupListUpdate()

//        val fm = supportFragmentManager
//        mGetMovieFrg = fm.findFragmentByTag(GetMoviesFragment.TAG) as GetMoviesFragment?

//        if (mGetMovieFrg == null) {
//            mGetMovieFrg = GetMoviesFragment()
//            fm.beginTransaction().add(mGetMovieFrg!!, GetMoviesFragment.TAG).commit()
//
//            getMoviePage(EndlessRecyclerViewScrollListener.STARTING_PAGE_INDEX)
//        } else {
//            onMoviesResult(mGetMovieFrg!!.retainMovies)
//        }
    }

    private fun getMoviePage(startingPageIndex: Int) {
        showLoading()
//        mGetMovieFrg!!.getData(startingPageIndex, mCurrentMovieOrderType)
    }

    private fun setUpViews() {

        showLoading()
//        mainViewModel.loading.set(View.VISIBLE)

//        btn_retry.setOnClickListener { getMoviePage(EndlessRecyclerViewScrollListener.STARTING_PAGE_INDEX) }

//        val layoutManager: GridLayoutManager

//        layoutManager = GridLayoutManager(this, calculateBestSpanCount())

//        getViewDataBinding().rvMovies.layoutManager = layoutManager

//        getViewDataBinding().rvMovies.setHasFixedSize(true)
//        moviesAdapter = MoviesAdapter()
//        moviesAdapter!!.setOnMovieClickListener(this)
//        getViewDataBinding().rvMovies.adapter = moviesAdapter




//        scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
//            public override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
//                getMoviePage(page)
//            }
//        }
//        getViewDataBinding().rvMovies.addOnScrollListener(scrollListener!!)


        mainViewModel.loading.set(View.VISIBLE)
        mainViewModel.getFirstPage()
        mainViewModel.getMovies().observe(this, Observer<List<MovieResult>> { movies ->
            mainViewModel.loading.set(View.INVISIBLE)
            if (movies.isEmpty()) {
                mainViewModel.showEmpty.set(View.VISIBLE)
            } else {

                mainViewModel.showEmpty.set(View.GONE)
                mainViewModel.setMoviesInAdapter(movies)
            }
        })


        mainViewModel.getSelectedMovie().observe(this, Observer<MovieResult> { movie ->
            if (movie != null) {
                onSelectedMovie(movie)
            }
        })

    }

    private fun showMovies() {
        mainViewModel.loading.set(View.INVISIBLE)
        mainViewModel.showEmpty.set(View.INVISIBLE)
    }

    override fun showLoading() {
        mainViewModel.loading.set(View.VISIBLE)
        mainViewModel.showEmpty.set(View.INVISIBLE)
    }

    private fun showLoadingError() {
        mainViewModel.loading.set(View.INVISIBLE)

        mainViewModel.showEmpty.set(View.VISIBLE)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_order_by) {

            val builderSingle = AlertDialog.Builder(this@MainActivity)
            builderSingle.setTitle(getString(R.string.select_order))


            val arrayAdapter = ArrayAdapter.createFromResource(this@MainActivity,
                    R.array.order_array,
                    android.R.layout.simple_list_item_1)

            builderSingle.setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.dismiss() }

            builderSingle.setAdapter(arrayAdapter) { dialog, which ->
                val newOrderType: AppApiHelper.MovieOrderType
                if (which == 0) {
                    newOrderType = AppApiHelper.MovieOrderType.POPULAR
                } else if (which == 1) {
                    newOrderType = AppApiHelper.MovieOrderType.TOP_RATED

                } else {
                    newOrderType = AppApiHelper.MovieOrderType.TOP_BOOKMARK
                }

                if (mCurrentMovieOrderType === newOrderType) {

                    getViewDataBinding().rvMovies.post { getViewDataBinding().rvMovies.smoothScrollToPosition(0) }

                } else {

                    mCurrentMovieOrderType = newOrderType

//                    mGetMovieFrg!!.resetMovieResults()
                    moviesAdapter!!.notifyDataSetChanged()
                    scrollListener!!.resetState()

                    getMoviePage(EndlessRecyclerViewScrollListener.STARTING_PAGE_INDEX)

                }
            }
            builderSingle.show()

            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }


    override fun onMovieError(throwable: Throwable) {
        showLoadingError()
    }

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun getViewModel(): MainViewModel {
        mainViewModel = ViewModelProviders.of(this,factory)
                .get(MainViewModel::class.java)
        return mainViewModel
    }

    override fun getBindingVariable(): Int = BR.viewModel


    override fun onSelectedMovie(movie: MovieResult) {
        val intent = MovieDetailActivity.newIntent(this@MainActivity)
        intent.putExtra(MovieResult::class.java.simpleName, movie)

        startActivity(intent)
    }

    override fun hideLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showToast(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
