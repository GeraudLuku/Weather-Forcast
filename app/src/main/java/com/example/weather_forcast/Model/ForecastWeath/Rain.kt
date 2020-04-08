package com.example.weather_forcast.Model.ForecastWeath


import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("3h")
    val h: Double
)