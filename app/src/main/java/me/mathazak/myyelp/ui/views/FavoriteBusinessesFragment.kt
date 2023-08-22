package me.mathazak.myyelp.ui.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import me.mathazak.myyelp.YelpApplication
import me.mathazak.myyelp.ui.adapters.BusinessesAdapter
import me.mathazak.myyelp.data.YelpBusiness
import me.mathazak.myyelp.databinding.FragmentFavoriteBusinessesBinding
import me.mathazak.myyelp.ui.viewmodels.BusinessViewModel
import me.mathazak.myyelp.ui.viewmodels.BusinessViewModelFactory

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
            ::onFavoriteIconClick,
            favoriteBusinesses::contains
        )

        viewModel.favoriteBusinesses.asLiveData()
            .observe(viewLifecycleOwner) { liveFavoriteBusinesses ->
                if (favoriteBusinesses.isEmpty()) {
                    favoriteBusinesses.addAll(liveFavoriteBusinesses)
                    adapter.submitList(favoriteBusinesses)
                } else {
                    for (i in favoriteBusinesses.indices) {
                        if (!liveFavoriteBusinesses.contains(favoriteBusinesses[i])) {
                            favoriteBusinesses.removeAt(i)
                            adapter.notifyItemRemoved(i)
                            break
                        }
                    }
                }
            }

        binding.rvFavoriteBusinesses.adapter = adapter
        binding.rvFavoriteBusinesses.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun onFavoriteIconClick(checked: Boolean, yelpBusiness: YelpBusiness) {
        if (checked)
            viewModel.insert(yelpBusiness)
        else {
            viewModel.delete(yelpBusiness)
        }
    }
}