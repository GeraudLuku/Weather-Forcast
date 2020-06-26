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
import android.provider.Settings
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather_forcast.Adapter.CitiesRecyclerAdapter
import com.example.weather_forcast.Model.City
import com.example.weather_forcast.Model.UserLocation
import com.example.weather_forcast.R
import com.example.weather_forcast.ViewModel.CitiesViewModel
import com.example.weather_forcast.ViewModel.WeatherViewModel
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.fragment_cities.*


class CitiesFragment : Fragment(), CitiesRecyclerAdapter.OnItemClickedListener {
    private val PERMISSION_ID = 42


    private lateinit var viewModelCities: CitiesViewModel
    private lateinit var viewModelWeather: WeatherViewModel

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var navController: NavController

    private var citiesList: ArrayList<City> = ArrayList()

    private lateinit var fadeInAnim: Animation
    private lateinit var fadeOutAnim: Animation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cities, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //set toolbar
        if (activity is AppCompatActivity) {
            setHasOptionsMenu(true)
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)

            Log.d("ActionBar", "Action Bar set")
        }

        //initialize view model class and navigation controller
        viewModelCities = ViewModelProvider(activity!!).get(CitiesViewModel::class.java)
        viewModelWeather = ViewModelProvider(activity!!).get(WeatherViewModel::class.java)
        navController = findNavController()

        //init fused location api
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context as Activity)


        //init animations
        fadeInAnim = AnimationUtils.loadAnimation(view.context, R.anim.fade_in)
        fadeOutAnim = AnimationUtils.loadAnimation(view.context, R.anim.fade_out)

        //observe list of cities from database
        viewModelCities.getCities()?.observe(viewLifecycleOwner, Observer { cities ->

            Log.d("Success- Cities", cities.toString())

            //check if list is not empty or null
            if (!cities.isEmpty()) {
                //notify data set changed
                //initialize recycler view and its adapters
                citiesList = cities as ArrayList<City>
                val adapter = CitiesRecyclerAdapter(citiesList, this)
                recycler_view.adapter = adapter
                recycler_view.layoutManager =
                    LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
                recycler_view.setHasFixedSize(true)


                //show list of cities view after everything has finished loading
                recycler_view.startAnimation(fadeInAnim)

                //hide progressbar when cities are been loaded
                progressBar.startAnimation(fadeOutAnim)
                progressBar.visibility = View.INVISIBLE
            } else {
                //hide progressbar if there are no cities
                progressBar.startAnimation(fadeOutAnim)
                progressBar.visibility = View.INVISIBLE
            }
        })

        //navigate to add city fragment
        add_city.setOnClickListener {
            //navigate
            navController.navigate(R.id.action_citiesFragment_to_addCityFragment)
        }

        //get current location weather
        current_location.setOnClickListener {
            //check if location permission is granted
            if (ContextCompat.checkSelfPermission(
                    activity!!.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ),
                    PERMISSION_ID
                )
            } else {
                getCurrentLocation()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                navController.popBackStack()
            }
            R.id.action_settings -> {
                //goto settings screen
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onItemCLicked(city: City) {
        //change current city weather
        viewModelWeather.setLocation(city.name)
        Log.d("Cities-Frag", "item clicked")
        navController.navigate(R.id.action_citiesFragment_to_homeFragment)
    }

    override fun onDeleteItem(city: City) {
        //change current city weather
        viewModelCities.deleteCity(city)
        Log.d("Cities-Frag", "item deleted")
    }

    private fun getCurrentLocation() {

        val locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 3000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        LocationServices.getFusedLocationProviderClient(activity!!)
            .requestLocationUpdates(
                locationRequest,
                object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult?) {
                        super.onLocationResult(locationResult)
                        LocationServices.getFusedLocationProviderClient(activity!!)
                            .removeLocationUpdates(this)
                        if (locationResult != null && locationResult.locations.size > 0) {

                            val latestLocationIndex = locationResult.locations.size - 1
                            val latitude = locationResult.locations[latestLocationIndex].latitude
                            val longitude = locationResult.locations[latestLocationIndex].longitude

                            //query weather information for location
                            viewModelWeather.setLocation(UserLocation(latitude, longitude))

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
                Toast.makeText(context, "Location Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
