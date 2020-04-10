package com.example.weather_forcast.Database

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weather_forcast.Model.City

interface CitiesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCity(city: City)

    @Query("SELECT * from cities_table ORDER BY id ASC")
    fun getCities(): LiveData<List<City>>

    @Delete
    fun deleteCity(city: City)

}