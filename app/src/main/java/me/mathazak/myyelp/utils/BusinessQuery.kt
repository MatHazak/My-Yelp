package me.mathazak.myyelp.utils

import me.mathazak.myyelp.data.YelpBusiness

interface BusinessQuery {
    fun isFavorite(yelpBusiness: YelpBusiness): Boolean
}