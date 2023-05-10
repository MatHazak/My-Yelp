package me.mathazak.myyelp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Switch
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
        setApi()
        setRecyclerView()
        searchBusinesses(YelpSearch("", "new york", "sandwich,seafood"))
        preferences = getPreferences(Context.MODE_PRIVATE)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        themeSwitch =
            menu?.findItem(R.id.themeSwitchBar)?.actionView?.findViewById(R.id.themeSwitch)!!
        updateUI()
        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            preferences.edit().putBoolean(getString(R.string.theme_switch), isChecked).apply()
            updateUI()
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.newSearch) {
            val intent = Intent(this, NewSearchActivity::class.java)
            searchActivityLauncher.launch(intent)
            // TODO: add last search to shared preferences
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
                result.data?.getSerializableExtra(getString(R.string.key_search)) as YelpSearch
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
            getString(R.string.authorization, getString(R.string.api_key)),
            yelpSearch.term,
            yelpSearch.location,
            yelpSearch.categories,
        ).enqueue(object : Callback<YelpSearchResult> {
            override fun onResponse(
                call: Call<YelpSearchResult>,
                response: Response<YelpSearchResult>
            ) {
                if (response.body() != null) {
                    showResults(response.body()!!.businesses)
                } else {
                    Log.e(
                        TAG, """""Didn't receive valid response.
                            HTTP status code: ${response.code()}
                            Error: ${response.errorBody()}""".trimIndent()
                    )
                }
            }

            override fun onFailure(call: Call<YelpSearchResult>, t: Throwable) {
                Log.e(TAG, "Request to server failed: $t ")
            }
        })
    }

    private fun showResults(businesses: List<YelpBusiness>) {
        businessesResult.clear()
        businessesResult.addAll(businesses)
        rvBusinesses.adapter!!.notifyDataSetChanged()
    }

    private fun updateUI() {
        themeSwitch.apply {
            isChecked = preferences.getBoolean(getString(R.string.theme_switch), false)
            if (isChecked) {
                setDefaultNightMode(MODE_NIGHT_YES)
                Log.d(TAG, "in menu item finder, switch checked in bar caught!!")

            } else {
                setDefaultNightMode(MODE_NIGHT_NO)
                Log.d(TAG, "in menu item finder, switch not checked in bar caught!!")
            }
        }
    }
}