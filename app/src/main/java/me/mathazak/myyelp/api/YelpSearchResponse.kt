package me.mathazak.myyelp.api

data class YelpSearchResponse(
    val total: Int,
    val businesses: List<NetworkBusiness>
)
