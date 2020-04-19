package com.example.weather_forcast.Features

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.weather_forcast.Model.City

import com.example.weather_forcast.R
import com.example.weather_forcast.ViewModel.CitiesViewModel
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import kotlinx.android.synthetic.main.fragment_add_city.*
import java.util.*


class AddCityFragment : Fragment() {

    private val AUTOCOMPLETE_REQUEST_CODE = 1

    private lateinit var viewModel: CitiesViewModel
    private lateinit var navController: NavController

    private var place: Place? = null
    private var placeName: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_city, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //initialize view model class and navigation controller
        viewModel = ViewModelProvider(activity!!).get(CitiesViewModel::class.java)
        navController = findNavController()

        //set toolbar
        if (activity is AppCompatActivity) {
            setHasOptionsMenu(true)
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        }

        //set click listener on edittext
        editText.setOnClickListener { view ->
            //open google places intent
            onSearchCalled()
        }

        //set click listener on cancel button
        imageButton.setOnClickListener {
            //remove selected city value
            editText.setText("")
            place = null
            placeName = ""
        }

        //set on Click listener on ok button
        imageButton2.setOnClickListener {
            if (!placeName.isBlank()) {
                //if place is not null add it to database
                viewModel.addCity(City(0, placeName))
                //navigate to cities list fragment
                navController.navigate(R.id.action_addCityFragment_to_citiesFragment)
            }

        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() == android.R.id.home) {
            navController.popBackStack()
        }

        return super.onOptionsItemSelected(item);
    }

    private fun onSearchCalled() {
        // Set the fields to specify which types of place data to return.
        val fields = listOf(Place.Field.NAME, Place.Field.LAT_LNG)

        //start the autocomplete intent
        val intent = context?.let {
            Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN,
                fields
            ).setCountry(Locale.getDefault().country)
                .build(it)
        }
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                //get selected place
                place = data?.let { Autocomplete.getPlaceFromIntent(it) }
                Log.d("Google Place API", place?.name)
                //set text on edit text
                editText.setText(place?.name)

                //set place
                placeName = place?.name.toString()

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {

                val status = data?.let { Autocomplete.getStatusFromIntent(it) }
                Log.i("Google Place APi", status?.statusMessage)

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
                Log.d("Google Place", "Operation canceled")
            }
        }
    }

}
