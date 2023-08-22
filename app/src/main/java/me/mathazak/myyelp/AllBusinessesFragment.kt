package me.mathazak.myyelp

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import me.mathazak.myyelp.data.YelpBusiness
import me.mathazak.myyelp.data.YelpSearchRequest
import me.mathazak.myyelp.databinding.FragmentAllBusinessesBinding

class AllBusinessesFragment : Fragment(), MenuProvider {
    private var _binding: FragmentAllBusinessesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BusinessViewModel by activityViewModels {
        BusinessViewModelFactory(
            (activity?.application as YelpApplication).repository
        )
    }

    private lateinit var themeSwitch: Switch
    private lateinit var preferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllBusinessesBinding.inflate(inflater, container, false)
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        preferences = requireActivity().getPreferences(AppCompatActivity.MODE_PRIVATE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val favoriteBusinesses = mutableListOf<YelpBusiness>()
        val adapter = BusinessesAdapter(
            viewModel::onFavoriteBusinessClick,
            favoriteBusinesses::contains
        )

        binding.rvSearchedBusinesses.adapter = adapter
        binding.rvSearchedBusinesses.layoutManager = LinearLayoutManager(requireContext())

        viewModel.favoriteBusinesses.asLiveData().observe(viewLifecycleOwner) {
            favoriteBusinesses.clear()
            favoriteBusinesses.addAll(it)

        }
        viewModel.searchedBusinesses.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        propSearchBar()
        binding.searchButton.setOnClickListener {
            binding.clSearch.visibility = View.GONE
            searchBusinesses()
        }

        searchBusinesses()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_main, menu)
        themeSwitch =
            menu.findItem(R.id.themeSwitchBar)?.actionView?.findViewById(R.id.themeSwitch)!!
        updateUi()
        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            preferences.edit().putBoolean(getString(R.string.theme_switch_key), isChecked).apply()
            updateUi()
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.newSearch -> {
                binding.clSearch.apply {
                    visibility = if (visibility == View.GONE)
                        View.VISIBLE
                    else
                        View.GONE
                }
                true
            }

            R.id.favorites -> {
                val action =
                    AllBusinessesFragmentDirections.actionAllBusinessesFragmentToFavoriteBusinessesFragment()
                findNavController().navigate(action)
                true
            }

            else -> false
        }
    }

    private fun searchBusinesses() {
        val term = binding.etTerm.editText?.text.toString()
        val location = binding.locationMenu.editText?.text.toString()
        val yelpSearchRequest = YelpSearchRequest(term, location)
        viewModel.fetchNewSearch(yelpSearchRequest)
    }

    private fun updateUi() {
        themeSwitch.apply {
            isChecked = preferences.getBoolean(getString(R.string.theme_switch_key), false)
            AppCompatDelegate.setDefaultNightMode(if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun propSearchBar() {
        val locationMenu = binding.locationMenu.editText as? AutoCompleteTextView
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.locations_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            locationMenu?.setAdapter(adapter)
            locationMenu?.setText(adapter.getItem(0), false)
        }
    }
}