package me.mathazak.myyelp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [YelpBusiness::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class BusinessRoomDatabase : RoomDatabase() {

    abstract fun businessDao(): BusinessDao

    companion object {
        @Volatile
        private var INSTANCE: BusinessRoomDatabase? = null

        fun getDatabase(context: Context): BusinessRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BusinessRoomDatabase::class.java,
                    "business_database",
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}