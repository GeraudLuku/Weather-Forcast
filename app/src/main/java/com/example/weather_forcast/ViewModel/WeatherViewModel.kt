package com.example.weather_forcast.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.weather_forcast.Api.RetrofitRepository
import com.example.weather_forcast.Model.CurrentWeath.CurrentWeather
import com.example.weather_forcast.Model.ForecastWeath.ForecastWeather

class WeatherViewModel : ViewModel() {

    private val _location: MutableLiveData<String> = MutableLiveData() //location variable

    //getting the current weather each time the _location value changes
    var currentWeather: LiveData<CurrentWeather> = Transformations
        .switchMap(_location) { _location ->
            RetrofitRepository.getCurrentWeather(_location)
        }

    //getting the forecast weather any time the _location value changes
    var forecastWeather: LiveData<ForecastWeather> = Transformations
        .switchMap(_location) { _location ->
            RetrofitRepository.getWeatherForecast(_location)
        }


    //setting the location name
    fun setLocation(location: String) {
        val updatedLocation = location
        if (_location.value == updatedLocation)
            return
        _location.value = updatedLocation
    }

    fun cancelJobs() {
        RetrofitRepository.cancelJobs()
    }
}