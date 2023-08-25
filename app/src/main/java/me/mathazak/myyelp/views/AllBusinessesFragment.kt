package me.mathazak.myyelp.views

import android.content.Context.INPUT_METHOD_SERVICE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import me.mathazak.myyelp.R
import me.mathazak.myyelp.YelpApplication
import me.mathazak.myyelp.adapters.BusinessesAdapter
import me.mathazak.myyelp.data.Business
import me.mathazak.myyelp.databinding.FragmentAllBusinessesBinding
import me.mathazak.myyelp.viewmodels.BusinessViewModel
import me.mathazak.myyelp.viewmodels.BusinessViewModelFactory

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

        var favoriteBusinessesId = listOf<String>()
        viewModel.favoriteBusinessesId
            .observe(viewLifecycleOwner) {
                favoriteBusinessesId = it
            }

        viewModel.fetchNewSearch()

        val adapter = BusinessesAdapter(
            ::onFavoriteIconClick,
        )
        viewModel.searchedBusinesses
            .observe(viewLifecycleOwner) {
                it.forEach { business ->
                    business.isFavorite = favoriteBusinessesId.contains(business.id)
                }
                adapter.submitList(it)
            }

        binding.rvSearchedBusinesses.adapter = adapter
        binding.rvSearchedBusinesses.layoutManager = LinearLayoutManager(requireContext())

        propSearchBar()
        binding.searchButton.setOnClickListener {
            binding.clSearch.visibility = View.GONE
            searchBusinesses()
        }
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

    private fun onFavoriteIconClick(checked: Boolean, business: Business) {
        if (checked)
            viewModel.insert(business)
        else
            viewModel.delete(business)
    }

    private fun searchBusinesses() {
        viewModel.apply {
            searchTerm = binding.etTerm.editText?.text.toString()
            searchLocation = binding.locationMenu.editText?.text.toString()
            fetchNewSearch()
        }
        val inputMethodManager = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}