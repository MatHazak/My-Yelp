package me.mathazak.myyelp.data

data class YelpSearchResult(
    val total: Int,
    val businesses: List<YelpBusiness>
)
