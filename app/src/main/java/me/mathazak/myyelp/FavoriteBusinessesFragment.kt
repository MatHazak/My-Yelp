package me.mathazak.myyelp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import me.mathazak.myyelp.data.YelpBusiness
import me.mathazak.myyelp.databinding.FragmentFavoriteBusinessesBinding

class FavoriteBusinessesFragment : Fragment() {

    private var _binding: FragmentFavoriteBusinessesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BusinessViewModel by activityViewModels {
        BusinessViewModelFactory(
            (activity?.application as YelpApplication).repository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBusinessesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val favoriteBusinesses = mutableListOf<YelpBusiness>()
        val adapter = BusinessesAdapter(
            viewModel::onFavoriteBusinessClick,
            favoriteBusinesses::contains
        )
        viewModel.favoriteBusinesses.asLiveData().observe(viewLifecycleOwner) {
            Log.d("FAV BUSI FRAGMENT", "looooooooooooooooop")
            favoriteBusinesses.clear()
            favoriteBusinesses.addAll(it)
            adapter.submitList(it)
        }

        binding.rvFavoriteBusinesses.adapter = adapter
        binding.rvFavoriteBusinesses.layoutManager = LinearLayoutManager(requireContext())

    }
}