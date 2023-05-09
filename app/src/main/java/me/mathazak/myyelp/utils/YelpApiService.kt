package me.mathazak.myyelp.utils

import me.mathazak.myyelp.models.YelpSearchResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface YelpApiService {
    @GET("businesses/search")
    fun searchBusinesses(
        @Header("authorization") authorization: String,
        @Query("term") searchTerm: String,
        @Query("location") location: String,
        @Query("categories") categories: String,
    ): Call<YelpSearchResult>
}