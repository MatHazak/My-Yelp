package me.mathazak.myyelp.models

import com.google.gson.annotations.SerializedName

data class YelpBusinessLocation(
    @SerializedName("address1") val address: String,
)
