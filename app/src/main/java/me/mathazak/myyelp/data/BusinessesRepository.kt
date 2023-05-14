package me.mathazak.myyelp.data

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import me.mathazak.myyelp.R
import me.mathazak.myyelp.YelpApplication
import me.mathazak.myyelp.utils.YelpApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "BusinessesRepository"

class BusinessesRepository(private val businessDao: BusinessDao) {

    private val yelpService =
        Retrofit.Builder().baseUrl(YelpApplication.getRes()?.getString(R.string.base_url) ?: "")
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(YelpApiService::class.java)


    val favoriteBusinesses = businessDao.loadAllBusinesses()
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
            getAuthorizationString(),
            yelpSearchRequest.term,
            yelpSearchRequest.location,
            yelpSearchRequest.categories,
        ).enqueue(object : Callback<YelpSearchResult> {
            override fun onResponse(
                call: Call<YelpSearchResult>,
                response: Response<YelpSearchResult>
            ) {
                if (response.body() != null)
                    searchedBusinesses.value = response.body()!!.businesses
                else
                    Log.e(
                        TAG,
                        """""Didn't receive valid response.
                            HTTP status code: ${response.code()}
                            Error: ${response.errorBody()}""".trimIndent()
                    )
            }

            override fun onFailure(call: Call<YelpSearchResult>, t: Throwable) {
                Log.e(TAG, "Request to server failed: $t ")
            }
        })
    }

    private fun getAuthorizationString(): String {
        return YelpApplication.getRes()?.let {
            it.getString(
                R.string.authorization_ph,
                it.getString(R.string.api_key)
            )
        } ?: ""
    }
}