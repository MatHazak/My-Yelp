package me.mathazak.myyelp

import android.app.Application
import me.mathazak.myyelp.remote.YelpApiService
import me.mathazak.myyelp.data.BusinessRoomDatabase
import me.mathazak.myyelp.data.BusinessesRepository

class YelpApplication : Application() {

    private val database by lazy { BusinessRoomDatabase.getDatabase(this) }
    val repository by lazy { BusinessesRepository(database.businessDao(), YelpApiService.create()) }
}