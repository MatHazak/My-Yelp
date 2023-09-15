package me.mathazak.myyelp.api

import me.mathazak.myyelp.utils.Constants.AUTHORIZATION
import me.mathazak.myyelp.utils.Constants.SEARCH_LOCATION
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface YelpApiService {
    @Headers("authorization: $AUTHORIZATION")
    @GET("businesses/search?location=$SEARCH_LOCATION")
    suspend fun searchBusinesses(
        @Query("term") term: String,
    ): Response<YelpSearchResponse>
}