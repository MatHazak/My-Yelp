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

    private lateinit var cbCategories: List<CheckBox>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_search)
        setLocationSpinner()
        addCategories()
        findViewById<Button>(R.id.searchButton).setOnClickListener { search() }
    }

    private fun addCategories() {
        cbCategories = listOf<CheckBox>(
            findViewById(R.id.cbPizza),
            findViewById(R.id.cbCafes),
            findViewById(R.id.cbBars),
            findViewById(R.id.cbDonuts),
            findViewById(R.id.cbIndian),
            findViewById(R.id.cbSalad),
            findViewById(R.id.cbSandwich),
            findViewById(R.id.cbSeafood),
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
        // ToDo: split string resources to ui and util resources for constants and ui elements
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun getCategories(): String {
        val categories = mutableListOf<String>()
        cbCategories.forEach {
            if (it.isChecked)
                categories.add(it.text.toString())
        }
        return categories.joinToString(",")
    }
}