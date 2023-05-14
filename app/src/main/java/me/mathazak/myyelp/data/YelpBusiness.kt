package me.mathazak.myyelp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.annotations.SerializedName

@Entity(tableName = "fav_businesses_table")
data class YelpBusiness(
    @PrimaryKey val id: String,
    val name: String,
    val rating: Double,
    val price: String,
    @SerializedName("review_count") val numberOfReviews: Int,
    @SerializedName("distance") val distanceInMeter: Double,
    @SerializedName("image_url") val imageUrl: String,
    val categories: List<YelpBusinessCategory>,
    val location: YelpBusinessLocation,
    var favorite: Boolean = false,
) {
    fun displayDistance():String {
        return "${(distanceInMeter % 1000).toInt()} Km"
    }
}

data class YelpBusinessCategory(
    val title: String
)

data class YelpBusinessLocation(
    @SerializedName("address1") val address: String,
)

class Converters {

    @TypeConverter
    fun fromLocationStamp(value: String?): YelpBusinessLocation? {
        return value?.let { YelpBusinessLocation(value)}
    }

    @TypeConverter
    fun toLocationStamp(location: YelpBusinessLocation?): String? {
        return location?.address
    }

    @TypeConverter
    fun fromCategoriesStamp(value: String?): List<YelpBusinessCategory>? {
        return value?.let { value.split("|").map { YelpBusinessCategory(it) }}
    }

    @TypeConverter
    fun toCategoriesStamp(categories: List<YelpBusinessCategory>?): String? {
        return categories?.joinToString("|") { it.title }
    }
}