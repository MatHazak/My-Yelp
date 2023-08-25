package me.mathazak.myyelp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [LocalBusiness::class], version = 1, exportSchema = false)
abstract class YelpDatabase : RoomDatabase() {

    abstract fun businessDao(): BusinessDao

    companion object {
        @Volatile
        private var INSTANCE: YelpDatabase? = null

        fun getDatabase(context: Context): YelpDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    YelpDatabase::class.java,
                    "business_database",
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}