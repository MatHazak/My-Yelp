package me.mathazak.myyelp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.mathazak.myyelp.models.YelpBusiness
import me.mathazak.myyelp.models.YelpSearchResult
import me.mathazak.myyelp.utils.BusinessesAdapter
import me.mathazak.myyelp.utils.YelpApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    private lateinit var yelpService: YelpApiService
    private var businessesResult = mutableListOf<YelpBusiness>()
    private lateinit var rvBusinesses: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setApi()
        setRecyclerView()
        searchBusinesses("vegan", "new york")
    }

    private fun setRecyclerView() {
        rvBusinesses = findViewById(R.id.rvBusinesses)
        rvBusinesses.adapter = BusinessesAdapter(this, businessesResult)
        rvBusinesses.layoutManager = LinearLayoutManager(this)
    }

    private fun setApi() {
        val retrofit = Retrofit.Builder().baseUrl(getString(R.string.base_url))
            .addConverterFactory(GsonConverterFactory.create()).build()
        yelpService = retrofit.create(YelpApiService::class.java)
    }

    private fun searchBusinesses(searchTerm: String, location: String) {
        yelpService.searchBusinesses(
            "Bearer ${getString(R.string.api_key)}",
            searchTerm,
            location).enqueue(object : Callback<YelpSearchResult> {
            override fun onResponse(call: Call<YelpSearchResult>, response: Response<YelpSearchResult>) {
                Log.i(TAG, "start on response")
                if (response.body() == null) {
                    Log.w(
                        TAG, """""Didn't receive valid response.
                            HTTP status code: ${response.code()}
                            Error: ${response.errorBody()}""".trimIndent()
                    )
                } else {
                    updateResults(response.body()!!.businesses)
                    Log.i(TAG, businessesResult.toString())
                }
            }
            override fun onFailure(call: Call<YelpSearchResult>, t: Throwable) {
                Log.e(TAG, "on failure: $t ")
            }
        })
    }

    private fun updateResults(businesses: List<YelpBusiness>) {
        businessesResult.addAll(businesses)
        rvBusinesses.adapter!!.notifyDataSetChanged()
    }
}