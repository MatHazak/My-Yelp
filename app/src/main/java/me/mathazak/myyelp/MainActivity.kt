package me.mathazak.myyelp

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Switch
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.mathazak.myyelp.models.YelpBusiness
import me.mathazak.myyelp.models.YelpSearch
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
    private val searchActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ::onSearchActivityResult,
    )
    private lateinit var themeSwitch: Switch
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        preferences = getPreferences(MODE_PRIVATE)
        setApi()
        setRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        themeSwitch =
            menu?.findItem(R.id.themeSwitchBar)?.actionView?.findViewById(R.id.themeSwitch)!!
        updateUi()
        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            preferences.edit().putBoolean(getString(R.string.theme_switch_key), isChecked).apply()
            updateUi()
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.newSearch) {
            val intent = Intent(this, NewSearchActivity::class.java)
            searchActivityLauncher.launch(intent)
            true
        } else
            super.onOptionsItemSelected(item)
    }

    override fun recreate() {
        finish()
        overridePendingTransition(
            androidx.appcompat.R.anim.abc_fade_in,
            androidx.appcompat.R.anim.abc_fade_out)
        startActivity(intent)
        overridePendingTransition(
            androidx.appcompat.R.anim.abc_fade_in,
            androidx.appcompat.R.anim.abc_fade_out)
    }

    private fun onSearchActivityResult(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            val newSearch =
                result.data?.getSerializableExtra(getString(R.string.search_activity_key)) as YelpSearch
            preferences.edit()
                .putString(getString(R.string.search_term_key), newSearch.term)
                .putString(getString(R.string.search_location_key), newSearch.location)
                .putString(getString(R.string.search_categories_key), newSearch.categories).apply()
            searchBusinesses(newSearch)
        } else
            Log.e(TAG, "Can't get desired result from search activity.")
    }

    private fun setRecyclerView() {
        rvBusinesses = this.findViewById(R.id.rvBusinesses)
        rvBusinesses.adapter = BusinessesAdapter(this, businessesResult)
        rvBusinesses.layoutManager = LinearLayoutManager(this)
    }

    private fun setApi() {
        val retrofit = Retrofit.Builder().baseUrl(getString(R.string.base_url))
            .addConverterFactory(GsonConverterFactory.create()).build()
        yelpService = retrofit.create(YelpApiService::class.java)
    }

    private fun searchBusinesses(yelpSearch: YelpSearch) {
        yelpService.searchBusinesses(
            getString(R.string.authorization_ph, getString(R.string.api_key)),
            yelpSearch.term,
            yelpSearch.location,
            yelpSearch.categories,
        ).enqueue(object : Callback<YelpSearchResult> {
            override fun onResponse(
                call: Call<YelpSearchResult>,
                response: Response<YelpSearchResult>
            ) {
                if (response.body() != null)
                    showSearchResults(response.body())
                else
                    Log.e(TAG,
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

    private fun showSearchResults(searchResult: YelpSearchResult?) {
        if (searchResult?.businesses != null) {
            businessesResult.clear()
            businessesResult.addAll(searchResult.businesses)
            rvBusinesses.adapter!!.notifyDataSetChanged()
        } else {
            Toast.makeText(this, "Can't get result from the server.", Toast.LENGTH_LONG).show()
        }
    }

    private fun updateUi() {
        themeSwitch.apply {
            isChecked = preferences.getBoolean(getString(R.string.theme_switch_key), false)
            setDefaultNightMode(if (isChecked) MODE_NIGHT_YES else MODE_NIGHT_NO)
        }
        val lastSearch = YelpSearch(
            preferences.getString(getString(R.string.search_term_key), "")!!,
            preferences.getString(getString(R.string.search_location_key), "New York")!!,
            preferences.getString(getString(R.string.search_categories_key), "")!!,
        )
        searchBusinesses(lastSearch)
    }
}