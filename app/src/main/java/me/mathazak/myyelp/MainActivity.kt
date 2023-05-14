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
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.recyclerview.widget.LinearLayoutManager
import me.mathazak.myyelp.databinding.ActivityMainBinding
import me.mathazak.myyelp.data.YelpBusiness
import me.mathazak.myyelp.data.YelpSearchRequest
import me.mathazak.myyelp.data.YelpSearchResult
import me.mathazak.myyelp.utils.YelpApiService

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var yelpService: YelpApiService
    private var businessesResult = mutableListOf<YelpBusiness>()
    private val searchActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ::onSearchActivityResult,
    )
    private val businessesViewModel: BusinessViewModel by viewModels {
        BusinessViewModelFactory((application as YelpApplication).repository)
    }
    private lateinit var themeSwitch: Switch
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferences = getPreferences(MODE_PRIVATE)

        val adapter = BusinessesAdapter()
        binding.rvSearchedBusinesses.adapter = adapter
        binding.rvSearchedBusinesses.layoutManager = LinearLayoutManager(this)

        businessesViewModel.likedBusinesses.observe(this) { it ->
            adapter.submitList(it)
        }

        businessesViewModel.searchedBusinesses.observe(this) {

        }
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
            androidx.appcompat.R.anim.abc_fade_out
        )
        startActivity(intent)
        overridePendingTransition(
            androidx.appcompat.R.anim.abc_fade_in,
            androidx.appcompat.R.anim.abc_fade_out
        )
    }

    private fun onSearchActivityResult(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            val newSearch =
                result.data?.getSerializableExtra(getString(R.string.search_activity_key)) as YelpSearchRequest
            preferences.edit()
                .putString(getString(R.string.search_term_key), newSearch.term)
                .putString(getString(R.string.search_location_key), newSearch.location)
                .putString(getString(R.string.search_categories_key), newSearch.categories).apply()
            searchBusinesses(newSearch)
        } else
            Log.e(TAG, "Can't get desired result from search activity.")
    }

    private fun setApi() {
    }

    private fun searchBusinesses(yelpSearchRequest: YelpSearchRequest) {
    }

    private fun showSearchResults(searchResult: YelpSearchResult?) {
        if (searchResult?.businesses != null) {
            businessesResult.clear()
            businessesResult.addAll(searchResult.businesses)
            binding.rvSearchedBusinesses.adapter!!.notifyDataSetChanged()
        } else {
            Toast.makeText(this, "Can't get result from the server.", Toast.LENGTH_LONG).show()
        }
    }

    private fun updateUi() {
        getString(R.string.authorization_ph, getString(R.string.api_key))
        themeSwitch.apply {
            isChecked = preferences.getBoolean(getString(R.string.theme_switch_key), false)
            setDefaultNightMode(if (isChecked) MODE_NIGHT_YES else MODE_NIGHT_NO)
        }
        val lastSearch = YelpSearchRequest(
            preferences.getString(getString(R.string.search_term_key), "")!!,
            preferences.getString(getString(R.string.search_location_key), "New York")!!,
            preferences.getString(getString(R.string.search_categories_key), "")!!,
        )
        searchBusinesses(lastSearch)
    }
}