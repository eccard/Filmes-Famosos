package com.eccard.popularmovies.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.eccard.popularmovies.data.network.api.ApiEmptyResponse
import com.eccard.popularmovies.data.network.api.ApiErrorResponse
import com.eccard.popularmovies.data.network.api.ApiResponse
import com.eccard.popularmovies.data.network.api.ApiSuccessResponse
import retrofit2.Call
import java.io.IOException

abstract class BaseFetchNextPage<Fetch,NetworkResponse> : Runnable {

    private val _liveData = MutableLiveData<Resource<Boolean>>()
    val liveData: LiveData<Resource<Boolean>> = _liveData

    protected var mFetch : Fetch? = null

    abstract fun findFetchInDb() : Fetch?

    abstract fun getNextPage() : Int?

    abstract fun createApi() : Call<NetworkResponse>?

    abstract fun onSuccessApiRequest(netResult: NetworkResponse, apiNextPage : Int?) : Unit

    private fun findFetchInDbAndCache() : Fetch? {
        mFetch = findFetchInDb()
        return mFetch
    }

    override fun run() {
        val current = findFetchInDbAndCache()
        if ( current == null){
            _liveData.postValue(null)
            return
        }

        val nextPage = getNextPage()
        if ( nextPage == null){
            _liveData.postValue(Resource.success(false))
            return
        }

        val newValue = try {

            val response = createApi()?.execute()


            response?.let {
                val apiResponse = ApiResponse.create(it)

                when (apiResponse) {
                    is ApiSuccessResponse -> {
                        onSuccessApiRequest(apiResponse.body,apiResponse.nextPage)
                        Resource.success(apiResponse.nextPage != null)
                    }

                    is ApiEmptyResponse -> {
                        Resource.success(false)
                    }

                    is ApiErrorResponse -> {
                        Resource.error(apiResponse.errorMessage, true)
                    }
                }
            }
        } catch (e : IOException){
            Resource.error(e.message!!,true)
        }

        _liveData.postValue(newValue)
    }


}