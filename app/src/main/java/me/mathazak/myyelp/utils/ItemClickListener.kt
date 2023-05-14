package me.mathazak.myyelp.utils

import me.mathazak.myyelp.data.YelpBusiness

interface ItemClickListener {
    fun onSwitchChange(checked: Boolean, yelpBusiness: YelpBusiness)
}