package me.mathazak.myyelp

import android.app.Application
import android.content.res.Resources
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import me.mathazak.myyelp.data.BusinessesRepository
import me.mathazak.myyelp.data.BusinessRoomDatabase

class YelpApplication: Application() {

    companion object {
        private var instance: YelpApplication? = null
        private var res: Resources? = null

        fun getRes(): Resources? {
            return res
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        res = resources
    }

    private val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy { BusinessRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { BusinessesRepository(database.businessDao()) }

}