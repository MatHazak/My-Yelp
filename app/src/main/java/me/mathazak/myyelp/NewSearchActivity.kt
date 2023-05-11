package me.mathazak.myyelp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import me.mathazak.myyelp.models.YelpSearch

class NewSearchActivity : AppCompatActivity() {

    private lateinit var cbCategories: List<Pair<CheckBox, String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_search)
        setLocationSpinner()
        addCategories()
        findViewById<Button>(R.id.searchButton).setOnClickListener { search() }
    }

    private fun addCategories() {
        cbCategories = listOf<Pair<CheckBox, String>>(
            Pair(findViewById(R.id.cbPizza), getString(R.string.api_pizza)),
            Pair(findViewById(R.id.cbCafes), getString(R.string.api_cafes)),
            Pair(findViewById(R.id.cbBars), getString(R.string.api_bars)),
            Pair(findViewById(R.id.cbDonuts), getString(R.string.api_donuts)),
            Pair(findViewById(R.id.cbIndian), getString(R.string.api_indian)),
            Pair(findViewById(R.id.cbSalad), getString(R.string.api_salad)),
            Pair(findViewById(R.id.cbSandwich), getString(R.string.api_sandwich)),
            Pair(findViewById(R.id.cbSeafood), getString(R.string.api_seafood)),
        )
    }

    private fun setLocationSpinner() {
        val spinner: Spinner = findViewById(R.id.locationSpinner)
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
        val term = findViewById<EditText>(R.id.etTerm).text.toString()
        val location = findViewById<Spinner>(R.id.locationSpinner).selectedItem.toString()
        val categories = getCategories()
        val newYelpSearch = YelpSearch(term, location, categories)
        val intent = Intent()
        intent.putExtra(getString(R.string.key_search), newYelpSearch)
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