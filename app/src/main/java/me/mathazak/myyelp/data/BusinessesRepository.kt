package me.mathazak.myyelp.data

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import me.mathazak.myyelp.api.YelpApiService
import me.mathazak.myyelp.utils.DataStatus
import me.mathazak.myyelp.utils.toBusiness
import me.mathazak.myyelp.utils.toLocalBusiness

class BusinessesRepository(
    private val businessDao: BusinessDao,
    private val yelpService: YelpApiService
) {

    private val _favoriteBusinesses = businessDao.getFavoriteBusinesses()
    val favoriteBusinesses: Flow<List<Business>>
        get() = _favoriteBusinesses.map { it.toBusiness() }

    @WorkerThread
    suspend fun insert(business: Business) {
        businessDao.insert(business.toLocalBusiness())
    }

    @WorkerThread
    suspend fun delete(business: Business) {
        businessDao.delete(business.toLocalBusiness())
    }

    @WorkerThread
    suspend fun searchBusinesses(
        searchTerm: String,
    ) = flow {
        try {
            val response = yelpService.searchBusinesses(searchTerm)
            if (response.isSuccessful) {
                val data = response.body()!!.businesses.toBusiness()
                emit(DataStatus.success(data))
            } else
                throw RuntimeException()

        } catch (exception: Exception) {
            emit(DataStatus.error())
        }
    }
}