package com.example.weather_forcast.Features

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather_forcast.Adapter.CitiesRecyclerAdapter
import com.example.weather_forcast.R
import com.example.weather_forcast.ViewModel.CitiesViewModel
import kotlinx.android.synthetic.main.fragment_cities.*


class CitiesFragment : Fragment() {

    private lateinit var viewModel: CitiesViewModel
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_cities, container, false)

        //set toolbar
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            (activity as AppCompatActivity).supportActionBar?.setDisplayShowCustomEnabled(true)
            (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)

            Log.d("ActionBar", "Action Bar set")
        }



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //initialize view model class and navigation controller
        viewModel = ViewModelProvider(activity!!).get(CitiesViewModel::class.java)
        navController = findNavController()

        //observe list of cities from database
        viewModel.getCities()?.observe(viewLifecycleOwner, Observer { cities ->

            Log.d("Success- Forecast", cities.toString())

            //notify data set changed
            //initialize recycler view
            recycler_view.adapter = CitiesRecyclerAdapter(view.context, cities)
            recycler_view.layoutManager =
                LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
            recycler_view.setHasFixedSize(true)
        })

        //navigate to add city fragment
        floatingActionButton.setOnClickListener {
            //navigate
            findNavController().navigate(R.id.action_citiesFragment_to_addCityFragment)
        }
    }

}
