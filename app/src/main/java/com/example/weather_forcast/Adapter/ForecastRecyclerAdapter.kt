package com.example.weather_forcast.Adapter

import android.R.string
import android.content.Context
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather_forcast.Model.ForecastWeath.Forecast
import com.example.weather_forcast.R
import java.text.SimpleDateFormat
import java.util.*


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
            .toString()  //convert to integer first so as to remove the decimal point

        //convert string to date
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        val date = format.parse(forecast.dtTxt)

        //"2020-01-07 15:00:00"
//        val date = DateTimeUtils.formatDate(forecast.dtTxt)

        //set time
        val formatTime = SimpleDateFormat("HH:mm a", Locale.ENGLISH)
        holder.time.text = formatTime.format(date)

        //set day
        val formatDate = SimpleDateFormat("EEEE", Locale.ENGLISH)
        holder.day.text = formatDate.format(date)

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
