package me.mathazak.myyelp.data

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import me.mathazak.myyelp.api.YelpApiService
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
    suspend fun searchBusinesses(searchTerm: String, searchLocation: String): Flow<List<Business>> {
        val response = yelpService.searchBusinesses(
            term = searchTerm,
            location = searchLocation,
        )
        return flow {
            when (response.code()) {
                200 -> emit(response.body()!!.businesses.toBusiness())
            }
        }
    }
}