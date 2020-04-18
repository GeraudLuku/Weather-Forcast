package com.example.weather_forcast.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.weather_forcast.Api.RetrofitRepository
import com.example.weather_forcast.Model.CurrentWeath.CurrentWeather
import com.example.weather_forcast.Model.ForecastWeath.ForecastWeather
import com.example.weather_forcast.Model.UserLocation

class WeatherViewModel : ViewModel() {

    public val _isLoading: MutableLiveData<Boolean> =
        MutableLiveData() //operation loading variable

    private val _location: MutableLiveData<String> = MutableLiveData() //location variable

    private val _current_user_location: MutableLiveData<UserLocation> =
        MutableLiveData() //current user location variable

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


    //getting the current weather for user each time the _location value changes
    var currentUserWeather: LiveData<CurrentWeather> = Transformations
        .switchMap(_current_user_location) { _current_user_location ->
            RetrofitRepository.getCurrentWeather(
                _current_user_location.latitude,
                _current_user_location.longitude
            )
        }

    //getting the forecast weather for user any time the _location value changes
    var currentUserForecastWeather: LiveData<ForecastWeather> = Transformations
        .switchMap(_current_user_location) { _current_user_location ->
            RetrofitRepository.getWeatherForecast(
                _current_user_location.latitude,
                _current_user_location.longitude
            )
        }


    //setting the location name
    fun setLocation(location: String) {
        val updatedLocation = location
        if (_location.value == updatedLocation)
            return

        //set is loading
        _isLoading.value = true

        _location.value = updatedLocation
    }

    //setting the current user location coordinates
    fun setLocation(location: UserLocation) {
        val updatedLocation = location
        if (_current_user_location.value == updatedLocation)
            return

        //set is loading
        _isLoading.value = true

        _current_user_location.value = updatedLocation
    }


    fun cancelJobs() {
        RetrofitRepository.cancelJobs()
    }
}