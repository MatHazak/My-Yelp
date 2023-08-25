package me.mathazak.myyelp.utils

import me.mathazak.myyelp.data.Business
import me.mathazak.myyelp.data.LocalBusiness
import me.mathazak.myyelp.api.NetworkBusiness

fun NetworkBusiness.toBusiness() = Business(
    id = id,
    name = name,
    rating = rating,
    price = price ?: "",
    numberOfReviews = numberOfReviews,
    distance = (distanceInMeter % 1000).toInt(),
    imageUrl = imageUrl,
    category = categories.getOrNull(0)?.title ?: "No Category",
    location = location.address,
    isFavorite = false
)

fun List<NetworkBusiness>.toBusiness(): List<Business> = this.map(NetworkBusiness::toBusiness)

fun Business.toLocalBusiness() = LocalBusiness(
    id = id,
    name = name,
    rating = rating,
    price = price,
    numberOfReviews = numberOfReviews,
    distance = distance,
    imageUrl = imageUrl,
    category = category,
    location = location
)

fun LocalBusiness.toBusiness() = Business(
    id = id,
    name = name,
    rating = rating,
    price = price,
    numberOfReviews = numberOfReviews,
    distance = distance,
    imageUrl = imageUrl,
    category = category,
    location = location,
    isFavorite = true // Local businesses are favorite by definition
)

@JvmName("localToDomain")
fun List<LocalBusiness>.toBusiness() = this.map(LocalBusiness::toBusiness)