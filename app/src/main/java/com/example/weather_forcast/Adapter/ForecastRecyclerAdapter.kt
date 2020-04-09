package com.example.weather_forcast.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather_forcast.Model.ForecastWeath.Forecast
import com.example.weather_forcast.R
import com.github.thunder413.datetimeutils.DateTimeUtils
import android.text.format.DateFormat;

//capitalize each string to uppercase https://stackoverflow.com/questions/35177726/how-to-change-first-letter-of-each-word-to-uppercase-in-textview-xml

class ForecastRecyclerAdapter(
    private val forecastList: List<Forecast>,
    private val context: Context
) :
    RecyclerView.Adapter<ForecastRecyclerAdapter.ForecastRecyclerAdapterViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ForecastRecyclerAdapterViewHolder {
        val itemVIew =
            LayoutInflater.from(parent.context).inflate(R.layout.forecast_item, parent, false)
        return ForecastRecyclerAdapterViewHolder(itemVIew)
    }


    override fun onBindViewHolder(holder: ForecastRecyclerAdapterViewHolder, position: Int) {
        val forecast = forecastList[position]

        //set icon
        Glide.with(context)
            .load("http://openweathermap.org/img/w/" + forecast.weather[0].icon + ".png")
            .into(holder.icon)
        //set temperature
        holder.temp.text = forecast.main.temp.toInt()
            .toString()  //convert to interger frst so as to remove the decimal point

        //"2020-01-07 15:00:00"
        val date = DateTimeUtils.formatDate(forecast.dtTxt)

        //set time
        holder.time.text = DateFormat.format("HH:mm a", date)

        //set day
        holder.day.text = DateFormat.format("EEEE", date)

    }

    override fun getItemCount(): Int {
       return forecastList.size
    }

    class ForecastRecyclerAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.weather_condition_icon)
        val temp: TextView = itemView.findViewById(R.id.temperature)
        val time: TextView = itemView.findViewById(R.id.time)
        val day: TextView = itemView.findViewById(R.id.day)
    }

}
