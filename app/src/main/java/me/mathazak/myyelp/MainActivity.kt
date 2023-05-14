package me.mathazak.myyelp

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.Switch
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.recyclerview.widget.LinearLayoutManager
import me.mathazak.myyelp.data.YelpBusiness
import me.mathazak.myyelp.data.YelpSearchRequest
import me.mathazak.myyelp.databinding.ActivityMainBinding
import me.mathazak.myyelp.utils.ItemClickListener

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val businessesViewModel: BusinessViewModel by viewModels {
        BusinessViewModelFactory((application as YelpApplication).repository)
    }
    private lateinit var cbCategoriesList: List<Pair<CheckBox, String>>

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var themeSwitch: Switch
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferences = getPreferences(MODE_PRIVATE)

        val adapter = BusinessesAdapter()
        adapter.setItemListener(object : ItemClickListener {
            override fun onSwitchChange(checked: Boolean, yelpBusiness: YelpBusiness) {
                if (checked)
                    businessesViewModel.insert(yelpBusiness)
                else
                    businessesViewModel.delete(yelpBusiness)
            }
        })
        binding.rvSearchedBusinesses.adapter = adapter
        binding.rvSearchedBusinesses.layoutManager = LinearLayoutManager(this)

        businessesViewModel.searchedBusinesses.observe(this) {
            adapter.submitList(it)
        }

        propSearchBar()
        binding.searchButton.setOnClickListener {
            binding.clSearch.visibility = GONE
            searchBusinesses()
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
        val term = binding.etTerm.text.toString()
        val location = binding.locationSpinner.selectedItem.toString()
        val categories =
            cbCategoriesList.filter { it.first.isChecked }.joinToString(",") { it.second }
        val yelpSearchRequest = YelpSearchRequest(term, location, categories)
        businessesViewModel.fetchNewSearch(yelpSearchRequest)
    }

    private fun propSearchBar() {
        val spinner = binding.locationSpinner
        ArrayAdapter.createFromResource(
            this,
            R.array.locations_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        cbCategoriesList = listOf(
            binding.cbPizza to getString(R.string.api_pizza),
            binding.cbCafes to getString(R.string.api_cafes),
            binding.cbBars to getString(R.string.api_bars),
            binding.cbDonuts to getString(R.string.api_donuts),
            binding.cbIndian to getString(R.string.api_indian),
            binding.cbSalad to getString(R.string.api_salad),
            binding.cbSandwich to getString(R.string.api_sandwich),
            binding.cbSeafood to getString(R.string.api_seafood),
        )
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