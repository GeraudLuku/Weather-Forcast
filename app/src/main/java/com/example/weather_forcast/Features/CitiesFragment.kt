package com.example.weather_forcast.Features

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.weather_forcast.R
import kotlinx.android.synthetic.main.fragment_cities.*


class CitiesFragment : Fragment() {

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

            Log.d("ActionBar","Action Bar set")
        }



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //navigate to add city fragment
        floatingActionButton.setOnClickListener {
            //navigate
            findNavController().navigate(R.id.action_citiesFragment_to_addCityFragment)
        }
    }

}
