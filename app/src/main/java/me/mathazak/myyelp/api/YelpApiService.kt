package me.mathazak.myyelp.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import me.mathazak.myyelp.utils.Constants.AUTHORIZATION
import me.mathazak.myyelp.utils.Constants.BASE_URL
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface YelpApiService {
    @GET("businesses/search")
    suspend fun searchBusinesses(
        @Header("authorization") authorization: String = AUTHORIZATION,
        @Query("term") term: String,
        @Query("location") location: String,
    ): Response<YelpSearchResponse>

    companion object {

        private val moshi by lazy {
            Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
        }
        private val retrofit by lazy {
            Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .baseUrl(BASE_URL)
                .build()
        }

        fun create(): YelpApiService = retrofit.create(YelpApiService::class.java)
    }
}