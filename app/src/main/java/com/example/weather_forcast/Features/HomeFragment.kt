package com.example.weather_forcast.Features

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.provider.Settings
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.weather_forcast.Adapter.ForecastRecyclerAdapter
import com.example.weather_forcast.R
import com.example.weather_forcast.ViewModel.WeatherViewModel
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

    private val PERMISSION_ID = 42

    private lateinit var viewModel: WeatherViewModel
    private lateinit var navController: NavController

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var fadeInAnim: Animation
    private lateinit var fadeOutAnim: Animation

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //init fused location api
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(view.context)
        getLastLocation()

        //initialize view model class and navigation controller
        viewModel = ViewModelProvider(activity!!).get(WeatherViewModel::class.java)
        navController = findNavController()

        //init animations
        fadeInAnim = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in)
        fadeOutAnim = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out)


        //observe current weather
        viewModel.currentWeather.observe(viewLifecycleOwner, Observer { currentWeather ->

            Log.d("Successful", currentWeather.toString())

            if (currentWeather != null) {

                //update UI
                location.text = currentWeather.name
                weather_condition.text = currentWeather.weather[0].description
                temperature.text = currentWeather.main.temp.toInt()
                    .toString() //cast i to interger to remove the decimal point
                Glide.with(view)
                    .load("http://openweathermap.org/img/w/" + currentWeather.weather[0].icon + ".png")
                    .into(weather_condition_icon)

                //show current weather view after everything has finished loading
                weather_container.startAnimation(fadeInAnim)
                weatherProgress.startAnimation(fadeOutAnim)
                weatherProgress.visibility = View.INVISIBLE
            }

        })

        //observe forecast weather
        viewModel.forecastWeather.observe(viewLifecycleOwner, Observer { forecastWeather ->
            Log.d("Success- Forecast", forecastWeather.toString())

            if (forecastWeather != null) {
                //notify data set changed
                //initialize recycler view
                recycler_view.adapter = ForecastRecyclerAdapter(forecastWeather.list, view.context)
                recycler_view.layoutManager =
                    LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
                recycler_view.setHasFixedSize(true)

                //show recycler view after everything has finished loading
                recycler_view.startAnimation(fadeInAnim)
                forecast_progress.startAnimation(fadeOutAnim)
                forecast_progress.visibility = View.INVISIBLE
            }
        })

        //set the town to get location
//        viewModel.setLocation("london")

        //initialize view
        list_btn.setOnClickListener {
            //navigate to list of cities page
            navController?.navigate(R.id.action_homeFragment_to_citiesFragment)
        }
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
                        val lat = location.latitude
                        val long = location.longitude
                        Log.d("Known Location", long.toString())
                    }
                }
            } else {
                //prompt user to turn on location of device
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context as Activity)
        fusedLocationClient!!.requestLocationUpdates(
            locationRequest, locationCallback,
            Looper.myLooper()
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val lastLocation: Location = locationResult.lastLocation
            // get location here
            Log.d("Known Location", lastLocation.toString())
        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager =
            context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                context as Activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context as Activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }

        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            context as Activity,
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
