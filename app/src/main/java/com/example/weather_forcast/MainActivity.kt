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
import androidx.core.app.ActivityCompat
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

        //create a new place client instance
        var placesClient = Places.createClient(this)

        //subscribe to Weather View Model
        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)

        //query current location
        //getLastLocation()

        //dummy query
//        viewModel.setLocation(UserLocation(4.155069, 9.246097))
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                //get location if permission is allowed and location is enabled
                fusedLocationClient.lastLocation.addOnCompleteListener { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        //get latitude and longitude
                        val userLocation = UserLocation(location.latitude, location.longitude)
                        Log.d("Known Location", userLocation.toString())

                        //make network request
                        viewModel.setLocation(userLocation)
                    }
                }
            } else {
                //prompt user to turn on location of device
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
                requestNewLocationData()
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient!!.requestLocationUpdates(
            locationRequest, locationCallback,
            Looper.myLooper()
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val lastLocation: Location = locationResult.lastLocation
            // get location here
            val userLocation = UserLocation(lastLocation.latitude, lastLocation.longitude)
            Log.d("Known Location", userLocation.toString())

            //make network request
            viewModel.setLocation(userLocation)
        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }

        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }
}
