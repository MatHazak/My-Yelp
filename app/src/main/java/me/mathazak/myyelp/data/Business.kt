package me.mathazak.myyelp.data

/**
 * Domain model of a Business
 */
data class Business(
    val id: String,
    val name: String,
    val rating: Double,
    val price: String,
    val numberOfReviews: Int,
    val distance: Int,
    val imageUrl: String,
    val category: String,
    val location: String,
    var isFavorite: Boolean,
)