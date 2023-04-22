package me.mathazak.myyelp.utils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
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
            itemView.findViewById<TextView>(R.id.tvBusinessName).text = business.name
            itemView.findViewById<RatingBar>(R.id.rbBusiness).rating = business.rating.toFloat()
            itemView.findViewById<TextView>(R.id.tvPrice).text = business.price
            itemView.findViewById<TextView>(R.id.tvReviewNumbers).text = context.getString(R.string.review_ph, business.numberOfReviews)
            itemView.findViewById<TextView>(R.id.tvAddress).text = business.location.address
            itemView.findViewById<TextView>(R.id.tvCategory).text = business.categories[0].title
            itemView.findViewById<TextView>(R.id.tvDistance).text = business.displayDistance()
            Glide.with(context).load(business.imageUrl)
                .placeholder(ColorDrawable(context.getColor(R.color.md_theme_dark_error)))
                .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(15)))
                .into(itemView.findViewById(R.id.ivBusiness))
        }
    }
}
