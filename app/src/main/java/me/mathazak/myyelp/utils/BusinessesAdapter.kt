package me.mathazak.myyelp.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import me.mathazak.myyelp.R
import me.mathazak.myyelp.models.YelpBusiness

class BusinessesAdapter(private val context: Context, private val businesses: List<YelpBusiness>) : RecyclerView.Adapter<BusinessesAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_business, parent, false))
    }

    override fun getItemCount() = businesses.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val business = businesses[position]
        holder.bind(business)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(business: YelpBusiness) {
            itemView.findViewById<TextView>(R.id.tvName).text = business.name
        }

    }
}
