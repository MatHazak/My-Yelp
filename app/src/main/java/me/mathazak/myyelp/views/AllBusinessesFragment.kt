package me.mathazak.myyelp.views

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import me.mathazak.myyelp.R
import me.mathazak.myyelp.adapters.BusinessesAdapter
import me.mathazak.myyelp.data.Business
import me.mathazak.myyelp.databinding.FragmentAllBusinessesBinding
import me.mathazak.myyelp.utils.DataStatus
import me.mathazak.myyelp.viewmodels.BusinessViewModel

@AndroidEntryPoint
class AllBusinessesFragment : Fragment(), MenuProvider {

    private var _binding: FragmentAllBusinessesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BusinessViewModel by activityViewModels()

    private lateinit var themeSwitch: Switch
    private lateinit var preferences: SharedPreferences
    private val adapter = BusinessesAdapter(::onFavoriteIconClick)

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

        viewModel.searchedBusinesses
            .observe(viewLifecycleOwner) { dataStatus ->
                when (dataStatus.status) {
                    DataStatus.Status.SUCCESS ->
                        setAdapterData(dataStatus.data!!, favoriteBusinessesId)

                    DataStatus.Status.ERROR -> showConnectionErrorIcon()
                }
            }

        binding.rvSearchedBusinesses.adapter = adapter
        binding.rvSearchedBusinesses.layoutManager = LinearLayoutManager(requireContext())

        binding.fabNewSearch.setOnClickListener { showSearchDialog() }
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

            R.id.favorites -> {
                val action =
                    AllBusinessesFragmentDirections.actionAllBusinessesFragmentToFavoriteBusinessesFragment()
                findNavController().navigate(action)
                true
            }

            else -> false
        }
    }

    private fun showSearchDialog() {
        val action =
            AllBusinessesFragmentDirections.actionAllBusinessesFragmentToNewSearchDialog()
        findNavController().navigate(action)
    }

    private fun setAdapterData(data: List<Business>, favoriteBusinessesId: List<String>) {
        binding.ivConnectionError.visibility = View.GONE
        binding.ivNoSearchResult.visibility = if (data.isEmpty()) View.VISIBLE else View.GONE

        data.forEach { business ->
            business.isFavorite = favoriteBusinessesId.contains(business.id)
        }
        adapter.submitList(data)
    }

    private fun onFavoriteIconClick(checked: Boolean, business: Business) {
        if (checked)
            viewModel.insert(business)
        else
            viewModel.delete(business)
    }

    private fun showConnectionErrorIcon() {
        adapter.submitList(listOf())
        binding.ivConnectionError.visibility = View.VISIBLE
    }

    private fun updateUi() {
        themeSwitch.apply {
            isChecked = preferences.getBoolean(getString(R.string.theme_switch_key), false)
            AppCompatDelegate.setDefaultNightMode(if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}