package com.example.eccard.filmesfamosos.ui.main

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.eccard.filmesfamosos.R
import com.example.eccard.filmesfamosos.data.network.api.AppApiHelper
import com.example.eccard.filmesfamosos.data.network.database.MovieDao
import com.example.eccard.filmesfamosos.data.network.model.network.MovieResponse
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


    var mCurrentMovieOrderType: AppApiHelper.MovieOrderType = AppApiHelper.MovieOrderType.POPULAR
        set(value) {
            if (mCurrentMovieOrderType != value) {
                moviesDataRepo.clear()

                val listToAdapter =
                        if (value == AppApiHelper.MovieOrderType.TOP_BOOKMARK){
                            moviesFromDb.toMutableList()
                        } else {
                            moviesDataRepo

                        }

                moviesDataRepo = listToAdapter
                moviesAdapter.setMovies(moviesDataRepo)
            }
            field = value
        }

    private var moviesDataRepo : MutableList<MovieResult> = mutableListOf()
    var moviesFromDb : List<MovieResult> = listOf()
        set(value){
            if (mCurrentMovieOrderType == AppApiHelper.MovieOrderType.TOP_BOOKMARK) {
                moviesDataRepo = value.toMutableList()
                moviesAdapter.notifyDataSetChanged()
            }
            field = value
        }

    private var moviesDataApi : MutableLiveData<List<MovieResult>>
    private var moviesLiveDataFromDb : LiveData<List<MovieResult>>


    private val _selected = MutableLiveData<Event<MovieResult>>()

    val selected : LiveData<Event<MovieResult>>
        get() = _selected

    private var moviesAdapter: MoviesAdapter
    var showEmpty: ObservableInt
    var loading: ObservableInt

    fun getApiMovies():MutableLiveData<List<MovieResult>>  = moviesDataApi
    fun getDataBaseMovies():LiveData<List<MovieResult>>  = moviesLiveDataFromDb

    init {
        moviesAdapter = MoviesAdapter(R.layout.movie_item_view_holder,this)
        showEmpty = ObservableInt(View.GONE)
        loading = ObservableInt(View.VISIBLE)
        moviesDataApi = MutableLiveData()
        moviesLiveDataFromDb = moviesDao.loadAllMovies()
        moviesAdapter.setMovies(moviesDataRepo)
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


    fun addMoviesFromApi(movies: List<MovieResult>){
        moviesDataRepo.addAll(movies)
    }

    fun getAdapter(): MoviesAdapter = moviesAdapter




    fun getFirstPage() {
        getData(EndlessRecyclerViewScrollListener.STARTING_PAGE_INDEX)
    }

    fun getData(pageIndex: Int) {
        val scope = CoroutineScope(Dispatchers.Main)

        if (mCurrentMovieOrderType !== AppApiHelper.MovieOrderType.TOP_BOOKMARK) {

            loading.set(View.VISIBLE)

            scope.launch(context = Dispatchers.Main) {

                val response = withContext(context = Dispatchers.IO) {
                    apiHelper.doGetMoviesApiCall(mCurrentMovieOrderType, pageIndex)
                }

                loading.set(View.INVISIBLE)
                if (response.isSuccessful){
                    moviesDataApi.value = (response.body() as MovieResponse).movieResults
                } else {
                    showEmpty.set(View.VISIBLE)
                }
            }
        }
    }
}