package com.example.weather_forcast


import com.example.weather_forcast.Model.CurrentWeather
import retrofit2.http.GET
import retrofit2.http.Query

//http://api.weatherstack.com/current?access_key=0d586fdd5b3778499c9addb95638bd85&query=New York& language = en

interface ApixuWeatherApiService {

    @GET("current?access_key=0d586fdd5b3778499c9addb95638bd85")
    suspend fun getCurrentWeather(
        @Query("query") location: String
    ): CurrentWeather


}