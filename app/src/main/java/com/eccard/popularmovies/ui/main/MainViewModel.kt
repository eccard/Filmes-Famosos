package com.eccard.popularmovies.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import com.eccard.popularmovies.data.network.api.AppApiHelper
import com.eccard.popularmovies.data.network.model.MovieResult
import com.eccard.popularmovies.data.repository.MovieRepository
import com.eccard.popularmovies.data.repository.Resource
import com.eccard.popularmovies.data.repository.Status
import com.eccard.popularmovies.ui.base.BaseViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor(private var movieRepository: MovieRepository):
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


    fun loadNextPage() {
        _orderType.value?.let { nextPageHandler.queryNextPage(it) }
    }

    fun refresh() {
        _orderType.value?.let {
            _orderType.value = it
        }
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