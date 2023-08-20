package me.mathazak.myyelp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import me.mathazak.myyelp.data.YelpBusiness
import me.mathazak.myyelp.databinding.ItemBusinessBinding
import me.mathazak.myyelp.utils.BusinessQuery
import me.mathazak.myyelp.utils.ItemClickListener

class BusinessesAdapter :
    ListAdapter<YelpBusiness, BusinessesAdapter.BusinessViewHolder>(businessComparator) {

    private lateinit var itemClickListener: ItemClickListener
    private lateinit var businessQuery: BusinessQuery

    companion object {
        private val businessComparator = object : DiffUtil.ItemCallback<YelpBusiness>() {
            override fun areItemsTheSame(oldItem: YelpBusiness, newItem: YelpBusiness) =
                oldItem === newItem

            override fun areContentsTheSame(oldItem: YelpBusiness, newItem: YelpBusiness) =
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
            itemBinding.favoriteSwitch.setOnCheckedChangeListener { _, checked ->
                itemClickListener.onSwitchChange(checked, getItem(viewHolder.layoutPosition))
            }
        }
    }

    override fun onBindViewHolder(holder: BusinessViewHolder, position: Int) {
        val business = getItem(position)
        holder.bind(business)
    }

    fun setItemListener(listener: ItemClickListener) {
        this.itemClickListener = listener
    }

    fun setBusinessQuery(query: BusinessQuery) {
        this.businessQuery = query
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
            val imageUri = business.imageUrl.toUri()
            itemBinding.ivBusiness.load(imageUri) {
                placeholder(R.drawable.loading_animation)
                transformations(
                    RoundedCornersTransformation()
                )
                error(R.drawable.ic_broken_image)
            }
            itemBinding.favoriteSwitch.isChecked = businessQuery.isFavorite(business)
        }
    }
}