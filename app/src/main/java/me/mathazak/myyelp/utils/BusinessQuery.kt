package me.mathazak.myyelp.utils

import me.mathazak.myyelp.data.YelpBusiness

fun interface BusinessQuery {
    fun isFavorite(yelpBusiness: YelpBusiness): Boolean
}