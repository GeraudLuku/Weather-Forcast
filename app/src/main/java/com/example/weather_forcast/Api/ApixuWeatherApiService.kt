package com.example.weather_forcast


import com.example.weather_forcast.Model.CurrentWeath.CurrentWeather
import com.example.weather_forcast.Model.ForecastWeath.ForecastWeather
import retrofit2.http.GET
import retrofit2.http.Query

//api key for open weather c29a17192b18dc3bea8eeb505999ae9e
// api.openweathermap.org/data/2.5/weather?q=london&appid=c29a17192b18dc3bea8eeb505999ae9e
interface ApixuWeatherApiService {

    @GET("weather?appid=c29a17192b18dc3bea8eeb505999ae9e&units=metric")
    suspend fun getCurrentWeather(
        @Query("q") location: String
    ): CurrentWeather

    @GET("forecast?&appid=c29a17192b18dc3bea8eeb505999ae9e&units=metric")
    suspend fun getWeatherForecast(
        @Query("q") location: String
    ): ForecastWeather

}