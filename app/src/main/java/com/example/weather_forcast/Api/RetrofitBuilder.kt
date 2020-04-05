package com.example.weather_forcast.Api

import com.example.weather_forcast.ApixuWeatherApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {

    const val BASE_URL = "http://api.weatherstack.com"

    val retrofitBuilder: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }

    val apiService: ApixuWeatherApiService by lazy {
        retrofitBuilder
            .build()
            .create(ApixuWeatherApiService::class.java)
    }
}