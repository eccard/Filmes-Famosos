package com.example.eccard.filmesfamosos.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eccard.filmesfamosos.data.network.api.AppApiHelper
import com.example.eccard.filmesfamosos.data.network.database.MovieDao
import com.example.eccard.filmesfamosos.ui.main.MainViewModel
import java.lang.IllegalArgumentException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ViewModelProviderFactory @Inject constructor(private var moviewDao: MovieDao,
                                                   private var apiHelper: AppApiHelper):
        ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(moviewDao,apiHelper) as T
        }
//        else {
//            if(modelClass.isAssignableFrom(PaymentViewModel::class.java)) {
//                return PaymentViewModel() as T
//            } else {
//                if(modelClass.isAssignableFrom(ReaderQRViewModel::class.java)) {
//                    return ReaderQRViewModel() as T
//                }
//            }
//        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}