package me.mathazak.myyelp

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import me.mathazak.myyelp.databinding.ItemBusinessBinding
import me.mathazak.myyelp.data.YelpBusiness

class BusinessesAdapter :
    ListAdapter<YelpBusiness, BusinessesAdapter.BusinessViewHolder>(businessComparator) {

    companion object {
        private val businessComparator = object : DiffUtil.ItemCallback<YelpBusiness>() {
            override fun areItemsTheSame(oldItem: YelpBusiness, newItem: YelpBusiness) = oldItem === newItem
            override fun areContentsTheSame(oldItem: YelpBusiness, newItem: YelpBusiness) = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusinessViewHolder {
        return BusinessViewHolder(
            ItemBusinessBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BusinessViewHolder, position: Int) {
        val business = getItem(position)
        holder.bind(business)
    }

    inner class BusinessViewHolder(private val itemBinding: ItemBusinessBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(business: YelpBusiness) {
            itemBinding.tvBusinessName.text = business.name
            itemBinding.rbBusiness.rating = business.rating.toFloat()
            itemBinding.tvPrice.text = business.price
            itemBinding.tvReviewNumbers.text =
                itemBinding.root.context.getString(R.string.review_ph, business.numberOfReviews)
            itemBinding.tvAddress.text = business.location.address
            itemBinding.tvCategory.text = business.categories.getOrNull(0)?.title ?: "No Category"
            itemBinding.tvDistance.text = business.displayDistance()
            Glide.with(itemBinding.root.context).load(business.imageUrl)
                .placeholder(ColorDrawable(itemBinding.root.context.getColor(R.color.md_theme_dark_error)))
                .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(15)))
                .into(itemView.findViewById(R.id.ivBusiness))
        }
    }
}