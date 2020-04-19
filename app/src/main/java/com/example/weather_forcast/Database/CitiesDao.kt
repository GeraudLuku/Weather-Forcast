package com.example.weather_forcast.Database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.weather_forcast.Model.City

@Dao
interface CitiesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCity(city: City)

    @Query("SELECT * from cities_table ORDER BY id DESC")
    fun getCities(): LiveData<List<City>>

    @Delete
    fun deleteCity(city: City)

}