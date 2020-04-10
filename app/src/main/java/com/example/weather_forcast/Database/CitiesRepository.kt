package com.example.weather_forcast.Database

import android.app.Application
import com.example.weather_forcast.Model.City
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class CitiesRepository(application: Application) : CoroutineScope {

    //initialise coroutines scope
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private var citiesDao: CitiesDao? = null

    //initialize database and DAO
    init {
        val citiesDatabase = CitiesDatabase.getDatabase(application)
        citiesDao = citiesDatabase?.CitiesDao()
    }

    //functions to get all cities
    fun getCities() = citiesDao?.getCities()

    //function to add a city
    fun addCity(city: City) {
        launch {
            addCityCoroutine(city)
        }
    }

    //function to delete a city
    fun deleteCity(city: City) {
        launch {
            deleteCityCoroutine(city)
        }
    }


    //coroutine to add a city
    private suspend fun addCityCoroutine(city: City) {
        withContext(Dispatchers.IO) {
            citiesDao?.addCity(city)
        }
    }

    //coroutine to delete a city
    private suspend fun deleteCityCoroutine(city: City) {
        withContext(Dispatchers.IO) {
            citiesDao?.deleteCity(city)
        }
    }
}