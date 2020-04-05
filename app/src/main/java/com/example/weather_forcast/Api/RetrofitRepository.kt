package com.example.weather_forcast.Api

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.weather_forcast.Model.CurrentWeather
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

object RetrofitRepository {

    var job: CompletableJob? = null

    fun getCurrentWeather(
        location: String
    ): LiveData<CurrentWeather> {
        Log.d("GetCurrentWeather", "getting weather")
        job = Job()
        return object : LiveData<CurrentWeather>() {
            override fun onActive() { //when this method is called do something
                super.onActive()
                job?.let { theJob ->
                    CoroutineScope(IO + theJob).launch {//get current weather on the background thread
                        val currentWeather: CurrentWeather =
                            RetrofitBuilder.apiService.getCurrentWeather(location)
                        withContext(Main) {
                            //set value on the main thread
                            value = currentWeather
                            Log.d("Method", currentWeather.toString())
                            theJob.complete()
                        }
                    }
                }
            }
        }
    }

    fun cancelJobs() {
        job?.cancel();
    }
}