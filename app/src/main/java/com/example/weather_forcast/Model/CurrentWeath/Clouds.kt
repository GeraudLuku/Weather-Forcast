package com.example.weather_forcast.Model.CurrentWeath


import com.google.gson.annotations.SerializedName

data class Clouds(
    @SerializedName("all")
    val all: Int
)