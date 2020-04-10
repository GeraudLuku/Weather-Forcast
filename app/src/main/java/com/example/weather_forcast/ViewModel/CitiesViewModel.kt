package com.example.weather_forcast.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.weather_forcast.Database.CitiesRepository
import com.example.weather_forcast.Model.City

class CitiesViewModel(application: Application) : AndroidViewModel(application) {

    // initialise the repository
    private var repository: CitiesRepository = CitiesRepository(application)

    //get all cities from database
    fun getCities() = repository.getCities()

    //add a city to the database
    fun addCity(city: City) {
        repository.addCity(city)
    }

    //delete a city from database
    fun deleteCity(city: City) {
        repository.deleteCity(city)
    }

}