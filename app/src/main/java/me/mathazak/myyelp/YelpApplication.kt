package me.mathazak.myyelp

import android.app.Application
import me.mathazak.myyelp.api.YelpApiService
import me.mathazak.myyelp.data.YelpDatabase
import me.mathazak.myyelp.data.BusinessesRepository

class YelpApplication : Application() {

    private val database by lazy { YelpDatabase.getDatabase(this) }
    val repository by lazy { BusinessesRepository(database.businessDao(), YelpApiService.create()) }
}