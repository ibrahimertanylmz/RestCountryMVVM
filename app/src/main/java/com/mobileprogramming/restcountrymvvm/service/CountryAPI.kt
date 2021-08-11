package com.mobileprogramming.restcountrymvvm.service

import com.mobileprogramming.restcountrymvvm.model.CountryModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

//https://restcountries.eu/rest/v2/name/holland

interface CountryAPI {
    @GET("rest/v2/name/{countryName}")
    fun getData(
        @Path("countryName") countryName: String
    ): Single<CountryModel>
}