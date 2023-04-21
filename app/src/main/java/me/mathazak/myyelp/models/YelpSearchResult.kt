package me.mathazak.myyelp.models

data class YelpSearchResult(
    val total: Int,
    val businesses: List<YelpBusiness>
)
