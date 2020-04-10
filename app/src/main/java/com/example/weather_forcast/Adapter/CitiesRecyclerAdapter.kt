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

class CitiesRecyclerAdapter(private val context: Context, private val cities: List<City>) :
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

        //set city name
        holder.city_name.text = city.name

        //set city country icon
        holder.map_icon.setImageResource(R.drawable.cameroon)
    }

    override fun getItemCount(): Int {
        return cities.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val map_icon: ImageView = itemView.findViewById(R.id.image)
        val city_name: TextView = itemView.findViewById(R.id.name)

    }

}