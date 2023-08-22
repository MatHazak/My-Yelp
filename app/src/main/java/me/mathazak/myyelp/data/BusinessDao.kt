package me.mathazak.myyelp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BusinessDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(business: YelpBusiness)

    @Delete
    suspend fun delete(business: YelpBusiness)

    @Query("SELECT * FROM favorite_businesses_table")
    fun getFavoriteBusinesses(): Flow<List<YelpBusiness>>
}