package me.mathazak.myyelp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.squareup.moshi.Json

@Entity(tableName = "favorite_businesses_table")
data class YelpBusiness(
    @PrimaryKey val id: String,
    val name: String,
    val rating: Double,
    val price: String?,
    @Json(name = "review_count") @ColumnInfo("number_of_Reviews")
    val numberOfReviews: Int,
    @Json(name = "distance") @ColumnInfo("distance_in_meter")
    val distanceInMeter: Double,
    @Json(name = "image_url") @ColumnInfo("image_url")
    val imageUrl: String,
    val categories: List<YelpBusinessCategory>,
    val location: YelpBusinessLocation,
) {
    fun displayDistance(): String {
        return "${(distanceInMeter % 1000).toInt()} Km"
    }
}

data class YelpBusinessCategory(
    val title: String
)

data class YelpBusinessLocation(
    @Json(name = "address1") val address: String,
)

class Converters {

    @TypeConverter
    fun fromLocationStamp(value: String?): YelpBusinessLocation? {
        return value?.let { YelpBusinessLocation(value) }
    }

    @TypeConverter
    fun toLocationStamp(location: YelpBusinessLocation?): String? {
        return location?.address
    }

    @TypeConverter
    fun fromCategoriesStamp(value: String?): List<YelpBusinessCategory>? {
        return value?.let { value.split("|").map { YelpBusinessCategory(it) } }
    }

    @TypeConverter
    fun toCategoriesStamp(categories: List<YelpBusinessCategory>?): String? {
        return categories?.joinToString("|") { it.title }
    }
}