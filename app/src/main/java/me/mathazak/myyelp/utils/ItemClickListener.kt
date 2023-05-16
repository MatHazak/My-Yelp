package me.mathazak.myyelp.utils

import me.mathazak.myyelp.data.YelpBusiness

fun interface ItemClickListener {
    fun onSwitchChange(checked: Boolean, yelpBusiness: YelpBusiness)
}