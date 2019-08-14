package com.eccard.popularmovies.ui.main

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import com.eccard.popularmovies.R
import com.eccard.popularmovies.data.network.api.AppApiHelper
import com.eccard.popularmovies.data.network.database.MovieDao
import com.eccard.popularmovies.data.network.model.network.MovieResponse
import com.eccard.popularmovies.data.network.model.MovieResult
import com.eccard.popularmovies.data.repository.MovieRepository
import com.eccard.popularmovies.data.repository.Resource
import com.eccard.popularmovies.data.repository.Status
import com.eccard.popularmovies.utils.AbsentLiveData
import com.eccard.popularmovies.utils.EndlessRecyclerViewScrollListener
import com.eccard.popularmovies.utils.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.eccard.popularmovies.ui.base.BaseViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor(private var moviesDao: MovieDao, private var apiHelper:
AppApiHelper,movieRepository: MovieRepository):
        BaseViewModel<MainNavigator>(){

    companion object {
        private val TAG = MainViewModel::class.java.simpleName
    }

    private val nextPageHandler = NextPageHandler(movieRepository)
    private val _orderType = MutableLiveData<AppApiHelper.MovieOrderType>()

    val orderType : LiveData<AppApiHelper.MovieOrderType> = _orderType

    fun setNewOrder(orderType: AppApiHelper.MovieOrderType){
        _orderType.value = orderType
    }

    val results: LiveData<Resource<List<MovieResult>>> = Transformations
            .switchMap(_orderType) { fetched ->
                    movieRepository.fetchMovies(fetched)
            }

    var mCurrentMovieOrderType: AppApiHelper.MovieOrderType = AppApiHelper.MovieOrderType
    .POPULAR
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
//                moviesAdapter.setMovies(moviesDataRepo)
            }
            field = value
        }

    private var moviesDataRepo : MutableList<MovieResult> = mutableListOf()
    var moviesFromDb : List<MovieResult> = listOf()
        set(value){
            if (mCurrentMovieOrderType == AppApiHelper.MovieOrderType.TOP_BOOKMARK) {
                moviesDataRepo = value.toMutableList()
//                moviesAdapter.setMovies(moviesDataRepo)
            }
            field = value
        }

    private var moviesDataApi : MutableLiveData<List<MovieResult>>
    private var moviesLiveDataFromDb : LiveData<List<MovieResult>>


    private val _selected = MutableLiveData<Event<MovieResult>>()

    val selected : LiveData<Event<MovieResult>>
        get() = _selected

//    private var moviesAdapter: MoviesAdapter
    var showEmpty: ObservableInt
    var loading: ObservableInt

    fun getApiMovies():MutableLiveData<List<MovieResult>>  = moviesDataApi
    fun getDataBaseMovies():LiveData<List<MovieResult>>  = moviesLiveDataFromDb

    init {
//        moviesAdapter = MoviesAdapter(R.layout.movie_item_view_holder,this)
        showEmpty = ObservableInt(View.GONE)
        loading = ObservableInt(View.VISIBLE)
        moviesDataApi = MutableLiveData()
        moviesLiveDataFromDb = moviesDao.loadAllMovies()
//        moviesAdapter.setMovies(moviesDataRepo)
//        getFirstPage()
    }


    fun onItemClick(index: Int?) {
        getMovieAt(index)?.let {
            _selected.value = Event(it)
        }
    }

    fun getMovieAt(index:Int?):MovieResult?{
        return if (index != null && results.value?.data?.size!! > index){
            results.value?.data!![index]
        } else null
    }


    fun addMoviesFromApi(movies: List<MovieResult>){
        moviesDataRepo.addAll(movies)
    }

//    fun getAdapter(): MoviesAdapter = moviesAdapter




    fun getFirstPage() {
//        getData(EndlessRecyclerViewScrollListener.STARTING_PAGE_INDEX)
    }

    fun getData(pageIndex: Int) {
        val scope = CoroutineScope(Dispatchers.Main)

        if (mCurrentMovieOrderType !== AppApiHelper.MovieOrderType.TOP_BOOKMARK) {

            loading.set(View.VISIBLE)

            scope.launch(context = Dispatchers.Main) {

                try {
                    val response = withContext(context = Dispatchers.IO) {
                        apiHelper.doGetMoviesApiCall(mCurrentMovieOrderType, pageIndex)
                    }


                    if (response.isSuccessful) {
                        moviesDataApi.value = (response.body() as MovieResponse).results
                    } else {
                        showEmpty.set(View.VISIBLE)
                    }
                } catch (e: Exception){
                    if(moviesDataRepo.size == 0){
                        showEmpty.set(View.VISIBLE)
                    }
                } finally {
                    loading.set(View.INVISIBLE)
                }
            }
        }
    }

    fun loadNextPage() {
        nextPageHandler.queryNextPage(mCurrentMovieOrderType)
    }

    class LoadMoreState(val isRunning: Boolean, val errorMessage: String?) {
        private var handledError = false

        val errorMessageIfNotHandled: String?
            get() {
                if (handledError) {
                    return null
                }
                handledError = true
                return errorMessage
            }
    }

    val loadMoreStatus: LiveData<LoadMoreState>
        get() = nextPageHandler.loadMoreState

    class NextPageHandler(private val repository: MovieRepository) : Observer<Resource<Boolean>> {
        private var nextPageLiveData: LiveData<Resource<Boolean>>? = null
        val loadMoreState = MutableLiveData<LoadMoreState>()
        private var orderType: AppApiHelper.MovieOrderType? = null
        private var _hasMore: Boolean = false
        val hasMore
            get() = _hasMore

        init {
            reset()
        }

        fun queryNextPage( orderType: AppApiHelper.MovieOrderType) {
            if (this.orderType == orderType) {
                return
            }
            unregister()
            this.orderType = orderType
            nextPageLiveData = repository.fetchNextPage(orderType)
            loadMoreState.value = LoadMoreState(
                    isRunning = true,
                    errorMessage = null
            )
            nextPageLiveData?.observeForever(this)
        }

        override fun onChanged(result: Resource<Boolean>?) {
            if (result == null) {
                reset()
            } else {
                when (result.status) {
                    Status.SUCCESS -> {
                        _hasMore = result.data == true
                        unregister()
                        loadMoreState.setValue(
                                LoadMoreState(
                                        isRunning = false,
                                        errorMessage = null
                                )
                        )
                    }
                    Status.ERROR -> {
                        _hasMore = true
                        unregister()
                        loadMoreState.setValue(
                                LoadMoreState(
                                        isRunning = false,
                                        errorMessage = result.message
                                )
                        )
                    }
                    Status.LOADING -> {
                        // ignore
                    }
                }
            }
        }

        private fun unregister() {
            nextPageLiveData?.removeObserver(this)
            nextPageLiveData = null
            if (_hasMore) {
                orderType = null
            }
        }

        fun reset() {
            unregister()
            _hasMore = true
            loadMoreState.value = LoadMoreState(
                    isRunning = false,
                    errorMessage = null
            )
        }
    }
}