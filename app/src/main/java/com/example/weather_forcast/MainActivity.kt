package com.example.weather_forcast

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weather_forcast.Api.RetrofitRepository
import com.example.weather_forcast.ViewModel.WeatherViewModel
import com.google.android.libraries.places.api.Places

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //initialize google places api
        if (!Places.isInitialized())
            Places.initialize(applicationContext, getString(R.string.api_key))

        //create a new place client instance
        var placesClient = Places.createClient(this)
    }
}
