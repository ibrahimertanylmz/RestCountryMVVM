package com.mobileprogramming.restcountrymvvm.service

import android.util.Log
import com.mobileprogramming.restcountrymvvm.model.CountryModel
import com.mobileprogramming.restcountrymvvm.model.CountryModelItem
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class CountryAPIService {
    //https://restcountries.eu/rest/v2/name/holland
    private val BASE_URL = "https://restcountries.eu/"
    val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CountryAPI::class.java)

    fun getDataService(countryName: String): Single<CountryModel> {
        return api.getData(countryName)
    }

}