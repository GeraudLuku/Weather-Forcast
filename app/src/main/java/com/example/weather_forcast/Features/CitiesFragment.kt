package com.example.weather_forcast.Features

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController

import com.example.weather_forcast.R
import kotlinx.android.synthetic.main.fragment_cities.*

/**
 * A simple [Fragment] subclass.
 */
class CitiesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_cities, container, false)

        //set toolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        val actionBar = (activity as AppCompatActivity).supportActionBar
        setHasOptionsMenu(true)
        actionBar?.setDisplayShowHomeEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)


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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            //go back
            findNavController().popBackStack()
        }
        return super.onOptionsItemSelected(item)
    }
}
