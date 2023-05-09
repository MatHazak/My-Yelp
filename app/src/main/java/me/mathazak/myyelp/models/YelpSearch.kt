package me.mathazak.myyelp.models

import java.io.Serializable

data class YelpSearch(
    val term: String,
    val location: String,
    val categories: String,
) : Serializable
