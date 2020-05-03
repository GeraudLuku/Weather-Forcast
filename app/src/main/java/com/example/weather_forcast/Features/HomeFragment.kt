package com.example.weather_forcast.Features

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.weather_forcast.Adapter.ForecastRecyclerAdapter
import com.example.weather_forcast.Model.CurrentWeath.CurrentWeather
import com.example.weather_forcast.Model.ForecastWeath.Forecast
import com.example.weather_forcast.R
import com.example.weather_forcast.ViewModel.WeatherViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.bottom_menu_sheet.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class HomeFragment : Fragment(), ForecastRecyclerAdapter.OnItemClickedListener {

    private lateinit var viewModel: WeatherViewModel
    private lateinit var navController: NavController

    private var currentWeather: CurrentWeather? = null

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

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

        //init bottom sheet
        bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet_layout)
        bottomSheetBehavior.setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // React to state change
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        //if its hidden set it back to collapsed
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        //set back day to today
                        textView.text = "Today"
                        //reload current location data
                        currentWeather?.let {
                            loadDataOnBottomSheet(it)
                        }
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // React to dragging events
            }
        })

        //init animations
        fadeInAnim = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        fadeOutAnim = AnimationUtils.loadAnimation(context, R.anim.fade_out)

        //observe current location current weather
        viewModel.currentUserWeather.observe(viewLifecycleOwner, Observer { currentWeather ->
            Log.d("Successful", currentWeather.toString())

            if (currentWeather != null) {

                //update UI
                loadCurrentWeatherData(currentWeather, view)

                //load data on bottom sheet
                loadDataOnBottomSheet(currentWeather)

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
                        ForecastRecyclerAdapter(forecastWeather.list, this, view.context)
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
                loadCurrentWeatherData(currentWeather, view)

                //load data on bottom sheet
                loadDataOnBottomSheet(currentWeather)

                //notify loading is over
                viewModel._isLoading.postValue(false)
            }

        })

        //observe forecast weather
        viewModel.forecastWeather.observe(viewLifecycleOwner, Observer { forecastWeather ->
            Log.d("Success- Forecast", forecastWeather.toString())

            if (forecastWeather != null) {
                //initialize recycler view
                recycler_view.adapter =
                    ForecastRecyclerAdapter(forecastWeather.list, this, view.context)
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
//                    recycler_view.startAnimation(fadeOutAnim)
//                    forecast_progress.startAnimation(fadeInAnim)
//                    forecast_progress.visibility = View.VISIBLE

                    //hide current weather when loading
                    weather_container.startAnimation(fadeOutAnim)
                    weatherProgress.startAnimation(fadeInAnim)
                    weatherProgress.visibility = View.VISIBLE

                } else {
                    //if it has finished loading hide progress bars and show views

                    //show recycler view after everything has finished loading
//                    recycler_view.startAnimation(fadeInAnim)
//                    forecast_progress.startAnimation(fadeOutAnim)
//                    forecast_progress.visibility = View.INVISIBLE

                    //show current weather view after everything has finished loading
                    weather_container.startAnimation(fadeInAnim)
                    weatherProgress.startAnimation(fadeOutAnim)
                    weatherProgress.visibility = View.INVISIBLE
                }
            }
        })

        //initialize views
        list_btn.setOnClickListener {
            //navigate to list of cities page
            navController.navigate(R.id.action_homeFragment_to_citiesFragment)
        }

        weather_container.setOnClickListener {
            //open bottom sheet and display data
            currentWeather?.let {
                loadDataOnBottomSheet(it)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }


    //load data on bottom sheet
    fun loadDataOnBottomSheet(weather: CurrentWeather) {

        textView.text = "Today"

        textView2.text =
            String.format(Locale.getDefault(), "Feels Like %s°C", weather.main.feelsLike.toString())

        //sunrise and sunset
        sunrise.text = formatTime(Date(TimeUnit.SECONDS.toMillis(weather.sys.sunrise.toLong())))
        sunset.text = formatTime(Date(TimeUnit.SECONDS.toMillis(weather.sys.sunset.toLong())))

        //max and min temperature
        min_temperature.text = weather.main.tempMin.toInt().toString()
        max_temp.text = weather.main.tempMax.toInt().toString()

        //visibility and humidity
        visibility.text =
            String.format(
                Locale.getDefault(),
                "%s Km",
                (weather.visibility * 0.001).toString()
            ) //convert meters to kilometers
        humidity.text =
            String.format(Locale.getDefault(), "%s %%", weather.main.humidity.toString())

        //wind speed
        wind_speed.text =
            String.format(Locale.getDefault(), "%s Meter/sec", weather.wind.speed.toString())
    }

    fun loadCurrentWeatherData(currentWeather: CurrentWeather, view: View) {

        this.currentWeather = currentWeather
        //update UI
        location.text = currentWeather.name
        weather_condition.text =
            currentWeather.weather[0].description.toUpperCase(Locale.getDefault())
        temperature.text = currentWeather.main.temp.toInt()
            .toString() //cast i to integer to remove the decimal point
        Glide.with(view)
            .load("http://openweathermap.org/img/wn/" + currentWeather.weather[0].icon + "@2x.png")
            .placeholder(R.drawable.weather_icon_placeholder)
            .into(weather_condition_icon)
    }

    private fun formatTime(sun: Date): String {
        val timeformat = SimpleDateFormat("h:m a", Locale.getDefault())
        return timeformat.format(sun)
    }

    override fun onWeatherItemCLicked(forecast: Forecast) {
        //load data on bottom sheet and open it

        //convert string to date
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = format.parse(forecast.dtTxt)

        //set day
        val formatDate = SimpleDateFormat("E, d MMM yyyy", Locale.getDefault())
        textView.text = formatDate.format(date)

        textView2.text =
            String.format(
                Locale.getDefault(),
                "Feels Like %s°C",
                forecast.main.feelsLike.toString()
            )

        //sunrise and sunset
        sunrise.text = "N/A"
        sunset.text = "N/A"

        //max and min temperature
        min_temperature.text = forecast.main.tempMin.toInt().toString()
        max_temp.text = forecast.main.tempMax.toInt().toString()

        //visibility and humidity
        visibility.text = "N/A"
        humidity.text =
            String.format(Locale.getDefault(), "%s %%", forecast.main.humidity.toInt().toString())

        //wind speed
        wind_speed.text =
            String.format(Locale.getDefault(), "%s Meter/sec", forecast.wind.speed.toString())

        //open bottom sheet
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }
}
