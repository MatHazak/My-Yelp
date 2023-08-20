package me.mathazak.myyelp

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Switch
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.recyclerview.widget.LinearLayoutManager
import me.mathazak.myyelp.data.YelpBusiness
import me.mathazak.myyelp.data.YelpSearchRequest
import me.mathazak.myyelp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val businessesViewModel: BusinessViewModel by viewModels {
        BusinessViewModelFactory((application as YelpApplication).repository)
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var themeSwitch: Switch
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferences = getPreferences(MODE_PRIVATE)

        var favoriteBusinesses = listOf<YelpBusiness>()
        val adapter = BusinessesAdapter(
            businessesViewModel::onFavoriteBusinessClick,
            favoriteBusinesses::contains
        )

        binding.rvSearchedBusinesses.adapter = adapter
        binding.rvSearchedBusinesses.layoutManager = LinearLayoutManager(this)

        businessesViewModel.favoriteBusinesses.observe(this) {
            favoriteBusinesses = it
        }

        businessesViewModel.searchedBusinesses.observe(this) {
            adapter.submitList(it)
        }

        propSearchBar()
        binding.searchButton.setOnClickListener {
            binding.clSearch.visibility = GONE
            searchBusinesses()
        }

        searchBusinesses()
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
        return when (item.itemId) {
            R.id.newSearch -> {
                binding.clSearch.apply {
                    visibility = if (visibility == GONE)
                        VISIBLE
                    else
                        GONE
                }
                true
            }

            R.id.favorites -> {
                startActivity(Intent(this, FavoriteBusinessesActivity::class.java))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun searchBusinesses() {
        val term = binding.etTerm.editText?.text.toString()
        val location = binding.locationMenu.editText?.text.toString()
        val yelpSearchRequest = YelpSearchRequest(term, location)
        businessesViewModel.fetchNewSearch(yelpSearchRequest)
    }

    private fun propSearchBar() {
        val locationMenu = binding.locationMenu.editText as? AutoCompleteTextView
        ArrayAdapter.createFromResource(
            this,
            R.array.locations_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            locationMenu?.setAdapter(adapter)
            locationMenu?.setText(adapter.getItem(0), false)
        }
    }

    private fun updateUi() {
        themeSwitch.apply {
            isChecked = preferences.getBoolean(getString(R.string.theme_switch_key), false)
            setDefaultNightMode(if (isChecked) MODE_NIGHT_YES else MODE_NIGHT_NO)
        }
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
}