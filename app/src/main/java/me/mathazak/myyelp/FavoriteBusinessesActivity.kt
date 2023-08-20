package me.mathazak.myyelp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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

        var favoriteBusinesses = listOf<YelpBusiness>()
        val adapter = BusinessesAdapter(
            businessesViewModel::onFavoriteBusinessClick,
            favoriteBusinesses::contains
        )

        binding.rvFavoriteBusinesses.adapter = adapter
        binding.rvFavoriteBusinesses.layoutManager = LinearLayoutManager(this)

        businessesViewModel.favoriteBusinesses.observe(this) {
            favoriteBusinesses = it
            adapter.submitList(it)
        }
    }
}