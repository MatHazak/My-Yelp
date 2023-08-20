package me.mathazak.myyelp.data

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import me.mathazak.myyelp.api.YelpApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "BusinessesRepository"

class BusinessesRepository(
    private val businessDao: BusinessDao,
    private val yelpService: YelpApiService
) {

    val favoriteBusinesses = businessDao.getFavoriteBusinesses()
    val searchedBusinesses = MutableLiveData<List<YelpBusiness>>()

    @WorkerThread
    suspend fun insert(business: YelpBusiness) {
        businessDao.insert(business)
    }

    @WorkerThread
    suspend fun delete(business: YelpBusiness) {
        businessDao.delete(business)
    }

    fun searchBusinesses(yelpSearchRequest: YelpSearchRequest) {
        yelpService.searchBusinesses(
            term = yelpSearchRequest.term,
            location = yelpSearchRequest.location,
        ).enqueue(object : Callback<YelpSearchResult> {
            override fun onResponse(
                call: Call<YelpSearchResult>,
                response: Response<YelpSearchResult>
            ) {
                if (response.body() != null)
                    searchedBusinesses.value = response.body()!!.businesses
                else
                    Log.e(
                        TAG, "Didn't receive valid response. HTTP status code: ${response.code()}"
                    )
            }

            override fun onFailure(call: Call<YelpSearchResult>, t: Throwable) {
                Log.e(TAG, "Request to server failed: $t ")
            }
        })
    }
}