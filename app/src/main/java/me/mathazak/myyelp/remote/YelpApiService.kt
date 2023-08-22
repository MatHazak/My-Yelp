package me.mathazak.myyelp.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import me.mathazak.myyelp.data.YelpBusiness
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import java.io.Serializable

interface YelpApiService {
    @GET("businesses/search")
    fun searchBusinesses(
        @Header("authorization") authorization: String = AUTHORIZATION,
        @Query("term") term: String,
        @Query("location") location: String,
    ): Call<YelpSearchResult>

    companion object {

        private const val BASE_URL = "https://api.yelp.com/v3/"
        private const val AUTHORIZATION =
            "Bearer _45em3yx8PMHlBvUty-dl0HXu7Zigfmgy3Sle2AfLgyySJ4-ppNY_1d8o7MU-G8CmRMgyLzOXURiLCadUoQGF2bT0ESrGTlIGkjdmS6aok1wbZSzDpAhD2uC2otCZHYx"

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

data class YelpSearchRequest(
    val term: String,
    val location: String,
) : Serializable

data class YelpSearchResult(
    val total: Int,
    val businesses: List<YelpBusiness>
)
