package me.mathazak.myyelp.data

import java.io.Serializable

data class YelpSearchRequest(
    val term: String,
    val location: String,
) : Serializable
