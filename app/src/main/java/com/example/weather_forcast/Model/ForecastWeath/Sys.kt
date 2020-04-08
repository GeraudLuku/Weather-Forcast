package com.example.weather_forcast.Model.ForecastWeath


import com.google.gson.annotations.SerializedName

data class Sys(
    @SerializedName("pod")
    val pod: String
)