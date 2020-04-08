package com.example.weather_forcast.Model.CurrentWeath


import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("3h")
    val h: Double
)