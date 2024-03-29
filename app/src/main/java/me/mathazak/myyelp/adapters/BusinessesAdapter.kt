package me.mathazak.myyelp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import me.mathazak.myyelp.R
import me.mathazak.myyelp.data.Business
import me.mathazak.myyelp.databinding.ItemBusinessBinding

class BusinessesAdapter(
    private val itemClickListener: (Boolean, Business) -> Unit,
) :
    ListAdapter<Business, BusinessesAdapter.BusinessViewHolder>(BusinessComparator) {

    companion object {
        private val BusinessComparator = object : DiffUtil.ItemCallback<Business>() {
            override fun areItemsTheSame(oldItem: Business, newItem: Business) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Business, newItem: Business) =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusinessViewHolder {
        val itemBinding = ItemBusinessBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BusinessViewHolder(itemBinding).also { viewHolder ->
            itemBinding.favoriteIcon.setOnCheckedChangeListener { _, checked ->
                val position = viewHolder.layoutPosition
                itemClickListener(checked, getItem(position))
            }
        }
    }

    override fun onBindViewHolder(holder: BusinessViewHolder, position: Int) {
        val business = getItem(position)
        holder.bind(business)
    }

    inner class BusinessViewHolder(private val itemBinding: ItemBusinessBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(business: Business) {
            itemBinding.tvBusinessName.text = business.name
            itemBinding.rbBusiness.rating = business.rating.toFloat()
            itemBinding.tvPrice.text = business.price
            itemBinding.tvReviewNumbers.text =
                itemBinding.root.context.getString(R.string.review_ph, business.numberOfReviews)
            itemBinding.tvAddress.text = business.location
            itemBinding.tvCategory.text = business.category
            itemBinding.tvDistance.text =
                itemBinding.root.context.getString(R.string.distance_in_km, business.distance)
            itemBinding.favoriteIcon.isChecked = business.isFavorite

            val imageUri = business.imageUrl.toUri()
            itemBinding.ivBusiness.load(imageUri) {
                placeholder(R.drawable.loading_animation)
                transformations(
                    RoundedCornersTransformation()
                )
                error(R.drawable.ic_broken_image)
            }
        }
    }
}