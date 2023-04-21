package me.mathazak.myyelp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import me.mathazak.myyelp.models.YelpSearchResult
import me.mathazak.myyelp.utils.YelpApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val retrofit = Retrofit.Builder().baseUrl(getString(R.string.base_url))
            .addConverterFactory(GsonConverterFactory.create()).build()
        val yelpService = retrofit.create(YelpApiService::class.java)
        yelpService.searchBusinesses("Bearer ${getString(R.string.api_key)}", "vegan", "new york").enqueue(object : Callback<YelpSearchResult> {
            override fun onResponse(call: Call<YelpSearchResult>, response: Response<YelpSearchResult>) {
                Log.e(TAG, "onResponse: $response ")
            }
            override fun onFailure(call: Call<YelpSearchResult>, t: Throwable) {
                Log.e(TAG, "onResponse: $t ")
            }
        })
    }
}