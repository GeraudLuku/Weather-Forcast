package com.example.weather_forcast.Features

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.example.weather_forcast.R
import com.example.weather_forcast.ViewModel.WeatherViewModel
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

    lateinit var viewModel: WeatherViewModel

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

        //initialize view model class
        viewModel = ViewModelProvider(activity!!).get(WeatherViewModel::class.java)
        viewModel.currentWeather.observe(viewLifecycleOwner, Observer { currentWeather ->

            if (currentWeather.current != null) {
                Log.d("Successful", currentWeather.toString())
                //update UI
                location.text = currentWeather.location.name
                weather_condition.text = currentWeather.current.weather_descriptions.get(0)
                temperature.text = currentWeather.current.temperature.toString()
            } else
                Log.d("Failed", "Result is null")

        })

        //set the town to get location
        viewModel.setLocation("london")

        //initialize view
        list_btn.setOnClickListener {
            //navigate to list of cities page
            findNavController().navigate(R.id.action_homeFragment_to_citiesFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.cancelJobs()
    }
}
