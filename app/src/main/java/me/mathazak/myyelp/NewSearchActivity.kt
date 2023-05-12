package me.mathazak.myyelp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.CheckBox
import me.mathazak.myyelp.databinding.ActivityNewSearchBinding
import me.mathazak.myyelp.models.YelpSearch

class NewSearchActivity : AppCompatActivity() {

    private lateinit var cbCategories: List<Pair<CheckBox, String>>
    private lateinit var binding: ActivityNewSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setLocationSpinner()
        addCategories()
        binding.searchButton.setOnClickListener { search() }
    }

    private fun addCategories() {
        cbCategories = listOf(
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

    private fun setLocationSpinner() {
        val spinner = binding.locationSpinner
        ArrayAdapter.createFromResource(
            this,
            R.array.locations_array,
            android.R.layout.simple_spinner_item
        ).also {adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }

    private fun search() {
        val term = binding.etTerm.text.toString()
        val location = binding.locationSpinner.selectedItem.toString()
        val categories = getCategories()
        val newYelpSearch = YelpSearch(term, location, categories)
        val intent = Intent()
        intent.putExtra(getString(R.string.search_activity_key), newYelpSearch)
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun getCategories(): String {
        val categories = mutableListOf<String>()
        cbCategories.forEach {
            if (it.first.isChecked)
                categories.add(it.second)
        }
        return categories.joinToString(",")
    }
}