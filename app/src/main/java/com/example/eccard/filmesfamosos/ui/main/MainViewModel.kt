package com.example.eccard.filmesfamosos.ui.main

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.eccard.filmesfamosos.R
import com.example.eccard.filmesfamosos.data.network.api.AppApiHelper
import com.example.eccard.filmesfamosos.data.network.database.MovieDao
import com.example.eccard.filmesfamosos.data.network.model.MovieResponse
import com.example.eccard.filmesfamosos.data.network.model.MovieResult
import com.example.eccard.filmesfamosos.utils.EndlessRecyclerViewScrollListener
import com.example.eccard.filmesfamosos.utils.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import muxi.kotlin.walletfda.ui.base.BaseViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor(private var moviesDao: MovieDao, private var apiHelper: AppApiHelper):
        BaseViewModel<MainNavigator>(){

    companion object {
        private val TAG = MainViewModel::class.java.simpleName
    }

    private var moviesDataRepo : MutableList<MovieResult> = mutableListOf()

    private var moviesData : MutableLiveData<List<MovieResult>>


    private val _selected = MutableLiveData<Event<MovieResult>>()

    val selected : LiveData<Event<MovieResult>>
        get() = _selected

    private var moviesAdapter: MoviesAdapter
    var showEmpty: ObservableInt
    var loading: ObservableInt

    fun getMovies():MutableLiveData<List<MovieResult>>  = moviesData

    fun getSelectedMovie() = selected

    init {
        moviesAdapter = MoviesAdapter(R.layout.movie_item_view_holder,this)
        showEmpty = ObservableInt(View.GONE)
        loading = ObservableInt(View.VISIBLE)
        moviesData = MutableLiveData()
        getFirstPage()
    }


    fun onItemClick(index: Int?) {
        getMovieAt(index)?.let {
            _selected.value = Event(it)
        }
    }

    fun getMovieAt(index:Int?):MovieResult?{
        return if (index != null && moviesDataRepo.size > index){
            moviesDataRepo[index]
        } else null
    }


    fun setMoviesInAdapter(movies: List<MovieResult>){

        if (mCurrentMovieOrderType != AppApiHelper.MovieOrderType.TOP_BOOKMARK){
            moviesDataRepo.addAll(movies)
        }

        this.moviesAdapter.setMovies(moviesDataRepo)
    }

    fun getAdapter(): MoviesAdapter = moviesAdapter


    var mCurrentMovieOrderType: AppApiHelper.MovieOrderType = AppApiHelper.MovieOrderType.POPULAR
        set(value) {
            if (mCurrentMovieOrderType != value) {
                moviesDataRepo.clear()
                moviesAdapter.notifyDataSetChanged()

            }
            field = value
        }

    fun getFirstPage() {
        getData(EndlessRecyclerViewScrollListener.STARTING_PAGE_INDEX)
    }

    fun getData(pageIndex: Int) {
        loading.set(View.VISIBLE)

        val scope = CoroutineScope(Dispatchers.Main)

            if (mCurrentMovieOrderType === AppApiHelper.MovieOrderType.TOP_BOOKMARK) {
                scope.launch(context = Dispatchers.Main) {

                    val response = withContext(context = Dispatchers.IO) {
                        moviesDao.loadAllMovies()
                    }

                    loading.set(View.INVISIBLE)

                    moviesData.value = response
                    moviesDataRepo = response.toMutableList()

                }
            } else {


                scope.launch(context = Dispatchers.Main) {

                    val response = withContext(context = Dispatchers.IO) {
                        apiHelper.doGetMoviesApiCall(mCurrentMovieOrderType, pageIndex)
                    }

                    loading.set(View.INVISIBLE)
                    if (response.isSuccessful){
                            moviesData.value = (response.body() as MovieResponse).movieResults
                    } else {
                        showEmpty.set(View.VISIBLE)
                    }
                }
            }
    }
}