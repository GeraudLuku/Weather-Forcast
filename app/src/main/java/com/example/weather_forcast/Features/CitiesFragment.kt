package com.example.weather_forcast.Features

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather_forcast.Adapter.CitiesRecyclerAdapter
import com.example.weather_forcast.Model.City
import com.example.weather_forcast.R
import com.example.weather_forcast.ViewModel.CitiesViewModel
import com.example.weather_forcast.ViewModel.WeatherViewModel
import kotlinx.android.synthetic.main.fragment_cities.*


class CitiesFragment : Fragment(), CitiesRecyclerAdapter.onItemClickedListener {

    private lateinit var viewModelCities: CitiesViewModel
    private lateinit var viewModelWeather: WeatherViewModel

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
                val adapter = CitiesRecyclerAdapter(cities as ArrayList<City>, this)
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
        addCity.setOnClickListener { view ->
            //navigate
            navController.navigate(R.id.action_citiesFragment_to_addCityFragment)
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


}
