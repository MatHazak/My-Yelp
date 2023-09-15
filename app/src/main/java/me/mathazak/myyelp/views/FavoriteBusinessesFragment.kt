package me.mathazak.myyelp.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import me.mathazak.myyelp.adapters.BusinessesAdapter
import me.mathazak.myyelp.data.Business
import me.mathazak.myyelp.databinding.FragmentFavoriteBusinessesBinding
import me.mathazak.myyelp.viewmodels.BusinessViewModel

@AndroidEntryPoint
class FavoriteBusinessesFragment : Fragment() {

    private var _binding: FragmentFavoriteBusinessesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BusinessViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBusinessesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = BusinessesAdapter(
            ::onFavoriteIconClick
        )

        viewModel.favoriteBusinesses
            .observe(viewLifecycleOwner) { favoriteBusinesses ->
                adapter.submitList(favoriteBusinesses)
                if (favoriteBusinesses.isNullOrEmpty())
                    binding.ivNoFavorite.visibility = View.VISIBLE
                else
                    binding.ivNoFavorite.visibility = View.GONE
            }

        binding.rvFavoriteBusinesses.adapter = adapter
        binding.rvFavoriteBusinesses.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun onFavoriteIconClick(checked: Boolean, business: Business) {
        if (checked)
            viewModel.insert(business)
        else {
            viewModel.delete(business)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}