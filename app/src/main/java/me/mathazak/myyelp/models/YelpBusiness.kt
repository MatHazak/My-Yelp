package me.mathazak.myyelp.models

import com.google.gson.annotations.SerializedName

data class YelpBusiness(
    val name: String,
    val rating: Double,
    val price: String,
    @SerializedName("review_counts") val numberOfReviews: Int,
    @SerializedName("distance") val distanceInMeter: Double,
    @SerializedName("image_url") val imageUrl: String,
    val categories: List<YelpBusinessCategory>,
    val location: YelpBusinessLocation,
) {
    fun displayDistance():String {
        return "${(distanceInMeter % 1000).toInt()} Km"
    }
}
