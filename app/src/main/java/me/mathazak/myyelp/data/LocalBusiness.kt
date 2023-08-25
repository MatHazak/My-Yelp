package me.mathazak.myyelp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_businesses_table")
data class LocalBusiness(
    @PrimaryKey val id: String,
    val name: String,
    val rating: Double,
    val price: String,
    @ColumnInfo("number_of_Reviews") val numberOfReviews: Int,
    val distance: Int,
    @ColumnInfo("image_url") val imageUrl: String,
    val category: String,
    val location: String
)