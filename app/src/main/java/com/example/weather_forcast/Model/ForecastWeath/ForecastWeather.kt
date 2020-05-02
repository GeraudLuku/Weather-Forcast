package com.example.weather_forcast.Model.ForecastWeath


import com.google.gson.annotations.SerializedName

data class ForecastWeather(
    @SerializedName("city")
    val city: City,
    @SerializedName("cnt")
    val cnt: Int,
    @SerializedName("cod")
    val cod: String,
    @SerializedName("list")
    val list: ArrayList<Forecast>,
    @SerializedName("message")
    val message: Int
)