package com.example.eccard.filmesfamosos.ui.main

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import kotlin.math.roundToInt

//import android.R



class MainActivity : BaseActivity<ActivityMainBinding,MainViewModel>(), LifecycleOwner,MainNavigator {

//    private var moviesAdapter: MoviesAdapter? = null
    private var scrollListener: EndlessRecyclerViewScrollListener? = null
//    private var mCurrentMovieOrderType: AppApiHelper.MovieOrderType = AppApiHelper.MovieOrderType.POPULAR

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
    }

//    private fun getMoviePage(startingPageIndex: Int) {
//        showLoading()
////        mGetMovieFrg!!.getData(startingPageIndex, mCurrentMovieOrderType)
//    }

    private fun setUpViews() {

        showLoading()

        setupRecyclerView()

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
//        getViewDataBinding().rvMovies.addOnScrollListener(scrollListener)
//        getViewDataBinding().rvMovies.addOnScrollListener(scrollListener!!)

        mainViewModel.getApiMovies().observe(this, Observer<List<MovieResult>> { movies ->
            mainViewModel.loading.set(View.INVISIBLE)
            if (movies.isEmpty()) {
                mainViewModel.showEmpty.set(View.VISIBLE)
            } else {
                mainViewModel.showEmpty.set(View.GONE)
                mainViewModel.addMoviesFromApi(movies)
                mainViewModel.getAdapter().notifyDataSetChanged()
            }
        })

        mainViewModel.selected.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                onSelectedMovie(it)
            }
        })

        mainViewModel.getDataBaseMovies().observe(this, Observer {
            mainViewModel.moviesFromDb = it
            Log.d("MainActivity","update moview from db" + it.toString())
//            mainViewModel.getAdapter().notifyDataSetChanged()
        })

    }

    private fun setupRecyclerView(){


        val posterWidth = mActivityMainBinding.rvMovies.context.resources.getDimension(R.dimen.img_view_recycler_view_holder_width)
        val windowManager = mActivityMainBinding.rvMovies.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)
        val screenWidth = outMetrics.widthPixels.toFloat()
        val bestSpanCount = (screenWidth / posterWidth).roundToInt()

        mActivityMainBinding.rvMovies.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(mActivityMainBinding.rvMovies.context,bestSpanCount)
        mActivityMainBinding.rvMovies.layoutManager = layoutManager
        mActivityMainBinding.rvMovies.setHasFixedSize(true)
        mActivityMainBinding.rvMovies.adapter =  mainViewModel.getAdapter()


        scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            public override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                mainViewModel.getData(page)
            }
        }
        mActivityMainBinding.rvMovies.addOnScrollListener(scrollListener!!)
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

                if (mainViewModel.mCurrentMovieOrderType === newOrderType) {

                    getViewDataBinding().rvMovies.post { getViewDataBinding().rvMovies.smoothScrollToPosition(0) }

                } else {
                    scrollListener!!.resetState()
                    mainViewModel.mCurrentMovieOrderType = newOrderType
                    mainViewModel.getFirstPage()
//                    getMoviePage(EndlessRecyclerViewScrollListener.STARTING_PAGE_INDEX)

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
