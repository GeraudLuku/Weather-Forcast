package com.example.weather_forcast.Model

import com.google.gson.annotations.SerializedName

data class Current(
    @SerializedName("observation_time") val observation_time: String,
    @SerializedName("temperature") val temperature: Int,
    @SerializedName("weather_code") val weather_code: Int,
    @SerializedName("weather_icons") val weather_icons: List<String>,
    @SerializedName("weather_descriptions") val weather_descriptions: List<String>,
    @SerializedName("wind_speed") val wind_speed: Int,
    @SerializedName("wind_degree") val wind_degree: Int,
    @SerializedName("wind_dir") val wind_dir: String,
    @SerializedName("pressure") val pressure: Int,
    @SerializedName("precip") val precip: Int,
    @SerializedName("humidity") val humidity: Int,
    @SerializedName("cloudcover") val cloudcover: Int,
    @SerializedName("feelslike") val feelslike: Int,
    @SerializedName("uv_index") val uv_index: Int,
    @SerializedName("visibility") val visibility: Int
)