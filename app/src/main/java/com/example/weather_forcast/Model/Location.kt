package com.example.weather_forcast.Model

import com.google.gson.annotations.SerializedName

data class Location (
    @SerializedName("name") val name : String,
    @SerializedName("country") val country : String,
    @SerializedName("region") val region : String,
    @SerializedName("lat") val lat : Double,
    @SerializedName("lon") val lon : Double,
    @SerializedName("timezone_id") val timezone_id : String,
    @SerializedName("localtime") val localtime : String,
    @SerializedName("localtime_epoch") val localtime_epoch : Int,
    @SerializedName("utc_offset") val utc_offset : Double
)