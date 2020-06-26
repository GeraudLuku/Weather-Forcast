package com.example.weather_forcast

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.weather_forcast.Model.UserLocation
import com.example.weather_forcast.ViewModel.WeatherViewModel
import com.google.android.gms.location.*
import com.google.android.libraries.places.api.Places

class MainActivity : AppCompatActivity() {

    private val PERMISSION_ID = 42

    private lateinit var viewModel: WeatherViewModel

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //initialize google places api
        if (!Places.isInitialized())
            Places.initialize(applicationContext, getString(R.string.api_key))

        //subscribe to Weather View Model
        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)

        //check if location permission is granted
        if (ContextCompat.checkSelfPermission(
                applicationContext, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                PERMISSION_ID
            )
        } else {
            getCurrentLocation()
        }

    }

    private fun getCurrentLocation() {

        val locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 3000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        LocationServices.getFusedLocationProviderClient(this@MainActivity)
            .requestLocationUpdates(
                locationRequest,
                object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult?) {
                        super.onLocationResult(locationResult)
                        LocationServices.getFusedLocationProviderClient(this@MainActivity)
                            .removeLocationUpdates(this)
                        if (locationResult != null && locationResult.locations.size > 0) {

                            val latestLocationIndex = locationResult.locations.size - 1
                            val latitude = locationResult.locations[latestLocationIndex].latitude
                            val longitude = locationResult.locations[latestLocationIndex].longitude

                            //query weather information for location
                            viewModel.setLocation(UserLocation(latitude, longitude))

                        }
                    }
                },
                Looper.getMainLooper()
            )

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            } else {
                Toast.makeText(this, "Location Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
