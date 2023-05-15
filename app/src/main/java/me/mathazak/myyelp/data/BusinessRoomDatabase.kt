package me.mathazak.myyelp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kotlinx.coroutines.CoroutineScope

@Database(entities = [YelpBusiness::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class BusinessRoomDatabase : RoomDatabase() {

    abstract fun businessDao(): BusinessDao

    companion object {
        @Volatile
        private var INSTANCE: BusinessRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): BusinessRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BusinessRoomDatabase::class.java,
                    "business_database",
                )
                    .addCallback(BusinessDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class BusinessDatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback()
}