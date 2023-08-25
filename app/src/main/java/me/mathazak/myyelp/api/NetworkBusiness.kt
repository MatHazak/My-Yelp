package me.mathazak.myyelp.api

import com.squareup.moshi.Json

data class NetworkBusiness(
    val id: String,
    val name: String,
    val rating: Double,
    val price: String?,
    @Json(name = "review_count") val numberOfReviews: Int,
    @Json(name = "distance") val distanceInMeter: Double,
    @Json(name = "image_url") val imageUrl: String,
    val categories: List<YelpBusinessCategory>,
    val location: YelpBusinessLocation,
)

data class YelpBusinessCategory(
    val title: String
)

data class YelpBusinessLocation(
    @Json(name = "address1") val address: String,
)