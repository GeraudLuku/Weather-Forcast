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
    private val cities: ArrayList<City>,
    private var clickListener: OnItemClickedListener
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
        holder.initialize(city, clickListener, holder.adapterPosition)

    }

    override fun getItemCount(): Int {
        return cities.size
    }

    fun getCity(position: Int): City = cities[position]

    fun removeItem(position: Int, action: OnItemClickedListener, city: City) {
        cities.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, cities.size)
        //alert callback
        action.onDeleteItem(city)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val city_name: TextView = itemView.findViewById(R.id.name)
        val delete: ImageView = itemView.findViewById(R.id.imageView)

        //init item click listener
        fun initialize(city: City, action: OnItemClickedListener, position: Int) {
            //initialize items
            //set city name
            city_name.text = city.name
            city_name.text = city.name.substring(0,1).toUpperCase() + city.name.substring(1).toLowerCase() //make first letter capital

            val position = position

            //implement click function
            itemView.setOnClickListener {
                action.onItemCLicked(city)
            }

            //delete action
            delete.setOnClickListener {
                removeItem(position, action, city)
            }
        }

    }

    interface OnItemClickedListener {
        fun onItemCLicked(city: City)
        fun onDeleteItem(city: City)
    }
}