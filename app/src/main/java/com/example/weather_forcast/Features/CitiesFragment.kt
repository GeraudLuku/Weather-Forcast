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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
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
            getLastLocation()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() == android.R.id.home) {
            navController.popBackStack()
        }

        return super.onOptionsItemSelected(item);
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
                        viewModelWeather.setLocation(userLocation)

                        //move to home fragment
                        navController.navigate(R.id.action_citiesFragment_to_homeFragment)
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
            val userLocation = UserLocation(lastLocation.latitude, lastLocation.longitude)
            Log.d("Known Location", userLocation.toString())

            //make network request
            viewModelWeather.setLocation(userLocation)

            //move to home fragment
            navController.navigate(R.id.action_citiesFragment_to_homeFragment)

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
