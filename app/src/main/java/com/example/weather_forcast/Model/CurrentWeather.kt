package com.example.weather_forcast.Model

import com.google.gson.annotations.SerializedName

data class CurrentWeather (
    @SerializedName("request") val request : Request,
    @SerializedName("location") val location : Location,
    @SerializedName("current") val current : Current
)