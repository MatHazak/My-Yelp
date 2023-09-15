package me.mathazak.myyelp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LocalBusiness::class], version = 1, exportSchema = false)
abstract class YelpDatabase : RoomDatabase() {
    abstract fun businessDao(): BusinessDao
}