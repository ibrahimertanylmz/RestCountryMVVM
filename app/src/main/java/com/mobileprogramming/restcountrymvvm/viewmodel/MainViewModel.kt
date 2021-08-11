package com.mobileprogramming.restcountrymvvm.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobileprogramming.restcountrymvvm.model.CountryModel
import com.mobileprogramming.restcountrymvvm.service.CountryAPIService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class MainViewModel : ViewModel() {
    private val CountryAPIService = CountryAPIService()
    private val disposable = CompositeDisposable()

    val country_data = MutableLiveData<CountryModel>()
    val country_error = MutableLiveData<Boolean>()
    val country_load = MutableLiveData<Boolean>()

    fun refreshData(countryName: String) {
        getDataFromAPI(countryName)
    }
    private fun getDataFromAPI(countryName: String) {
        country_load.value = true
        disposable.add(
            CountryAPIService.getDataService(countryName)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CountryModel>() {
                    override fun onSuccess(t: CountryModel) {
                        country_error.value = false
                        country_load.value = false
                        country_data.value = t
                    }
                    override fun onError(e: Throwable) {
                        Log.d("MainViewModel", "onError -> $e")
                        country_error.value = true
                        country_load.value = false
                    }
                })
        )
    }
}