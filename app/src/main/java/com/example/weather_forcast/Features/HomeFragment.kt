package com.example.weather_forcast.Features

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
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
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*


class HomeFragment : Fragment() {

    private lateinit var viewModel: WeatherViewModel
    private lateinit var navController: NavController

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

        //initialize view model class and navigation controller
        viewModel = ViewModelProvider(activity!!).get(WeatherViewModel::class.java)
        navController = findNavController()


        //init animations
        fadeInAnim = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        fadeOutAnim = AnimationUtils.loadAnimation(context, R.anim.fade_out)

        //observe current location current weather
        viewModel.currentUserWeather.observe(viewLifecycleOwner, Observer { currentWeather ->
            Log.d("Successful", currentWeather.toString())

            if (currentWeather != null) {
                //update UI
                location.text = currentWeather.name
                weather_condition.text =
                    currentWeather.weather[0].description.toUpperCase(Locale.getDefault())
                temperature.text = currentWeather.main.temp.toInt()
                    .toString() //cast i to interger to remove the decimal point
                Glide.with(view)
                    .load("http://openweathermap.org/img/w/" + currentWeather.weather[0].icon + ".png")
                    .placeholder(R.drawable.weather_icon_placeholder)
                    .into(weather_condition_icon)

                //notify loading is over
                viewModel._isLoading.postValue(false)
            }
        })

        //observe current location forecast weather
        viewModel.currentUserForecastWeather.observe(
            viewLifecycleOwner,
            Observer { forecastWeather ->
                Log.d("Success- Forecast", forecastWeather.toString())

                if (forecastWeather != null) {
                    //notify data set changed
                    //initialize recycler view
                    recycler_view.adapter =
                        ForecastRecyclerAdapter(forecastWeather.list, view.context)
                    recycler_view.layoutManager =
                        LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
                    recycler_view.setHasFixedSize(true)

                    //notify loading is over
                    viewModel._isLoading.postValue(false)
                }
            })

        //observe current weather
        viewModel.currentWeather.observe(viewLifecycleOwner, Observer { currentWeather ->

            Log.d("Successful", currentWeather.toString())

            if (currentWeather != null) {

                //update UI
                location.text = currentWeather.name
                weather_condition.text =
                    currentWeather.weather[0].description.toUpperCase(Locale.getDefault())
                temperature.text = currentWeather.main.temp.toInt()
                    .toString() //cast i to integer to remove the decimal point
                Glide.with(view)
                    .load("http://openweathermap.org/img/w/" + currentWeather.weather[0].icon + ".png")
                    .placeholder(R.drawable.weather_icon_placeholder)
                    .into(weather_condition_icon)

                //notify loading is over
                viewModel._isLoading.postValue(false)
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

                //notify loading is over
                viewModel._isLoading.postValue(false)
            }
        })


        //listen to loading state
        viewModel._isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            isLoading?.let { isLoading ->
                if (isLoading) {
                    //if something is loading then hide views and show progress bar

                    //hide recycler view when loading
                    recycler_view.startAnimation(fadeOutAnim)
                    forecast_progress.startAnimation(fadeInAnim)
                    forecast_progress.visibility = View.VISIBLE

                    //hide current weather when loading
                    weather_container.startAnimation(fadeOutAnim)
                    weatherProgress.startAnimation(fadeInAnim)
                    weatherProgress.visibility = View.VISIBLE

                } else {
                    //if it has finished loading hide progress bars and show views

                    //show recycler view after everything has finished loading
                    recycler_view.startAnimation(fadeInAnim)
                    forecast_progress.startAnimation(fadeOutAnim)
                    forecast_progress.visibility = View.INVISIBLE

                    //show current weather view after everything has finished loading
                    weather_container.startAnimation(fadeInAnim)
                    weatherProgress.startAnimation(fadeOutAnim)
                    weatherProgress.visibility = View.INVISIBLE
                }
            }
        })

        //initialize view
        list_btn.setOnClickListener {
            //navigate to list of cities page
            navController?.navigate(R.id.action_homeFragment_to_citiesFragment)
        }
    }

}
