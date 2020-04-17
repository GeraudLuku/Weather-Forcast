package com.example.weather_forcast.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather_forcast.Model.City
import com.example.weather_forcast.R

class CitiesRecyclerAdapter(
    private val cities: List<City>,
    private var clickListener: onItemClickedListener
) :
    RecyclerView.Adapter<CitiesRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CitiesRecyclerAdapter.ViewHolder {
        val rootView =
            LayoutInflater.from(parent.context).inflate(R.layout.city_item, parent, false)
        return ViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: CitiesRecyclerAdapter.ViewHolder, position: Int) {
        val city = cities[position]
        //set click listener
        holder.initialize(city, clickListener)

    }

    override fun getItemCount(): Int {
        return cities.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val city_name: TextView = itemView.findViewById(R.id.name)

        //init item click listener
        fun initialize(city: City, action: onItemClickedListener) {
            //initialize items
            //set city name
            city_name.text = city.name

            //implement click function
            itemView.setOnClickListener {
                action.onItemCLicked(city)
            }
        }

    }

    interface onItemClickedListener {
        fun onItemCLicked(city: City)
    }
}