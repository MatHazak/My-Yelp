package me.mathazak.myyelp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import me.mathazak.myyelp.data.YelpBusiness
import me.mathazak.myyelp.databinding.ActivityFavoriteBusinessesBinding

class FavoriteBusinessesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBusinessesBinding
    private val businessesViewModel: BusinessViewModel by viewModels {
        BusinessViewModelFactory((application as YelpApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBusinessesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = BusinessesAdapter()
        adapter.setItemListener { checked, yelpBusiness ->
            businessesViewModel.apply {
                if (checked)
                    insert(yelpBusiness)
                else
                    delete(yelpBusiness)
            }
        }
        var favoriteBusinesses = listOf<YelpBusiness>()
        adapter.setBusinessQuery(favoriteBusinesses::contains)
        binding.rvFavoriteBusinesses.adapter = adapter
        binding.rvFavoriteBusinesses.layoutManager = LinearLayoutManager(this)

        businessesViewModel.favoriteBusinesses.observe(this) {
            favoriteBusinesses = it
            adapter.submitList(it)
        }
    }
}