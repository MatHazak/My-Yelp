package me.mathazak.myyelp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.mathazak.myyelp.data.Business
import me.mathazak.myyelp.data.BusinessesRepository
import me.mathazak.myyelp.utils.Constants.DEFAULT_SEARCH_LOCATION
import me.mathazak.myyelp.utils.Constants.DEFAULT_SEARCH_TERM
import me.mathazak.myyelp.utils.DataStatus

class BusinessViewModel(private val repository: BusinessesRepository) : ViewModel() {

    var searchTerm = DEFAULT_SEARCH_TERM
    var searchLocation = DEFAULT_SEARCH_LOCATION

    val favoriteBusinesses = repository.favoriteBusinesses.asLiveData()
    val favoriteBusinessesId: LiveData<List<String>>
        get() = favoriteBusinesses.map { favoriteBusinesses ->
            favoriteBusinesses.map { it.id }
        }
    private val _searchedBusinesses = MutableLiveData<DataStatus<List<Business>>>()

    val searchedBusinesses: LiveData<DataStatus<List<Business>>>
        get() = _searchedBusinesses

    fun insert(business: Business) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(business)
        }
    }

    fun delete(business: Business) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(business)
        }
    }

    fun fetchNewSearch() = viewModelScope.launch {
        repository.searchBusinesses(searchTerm, searchLocation).collect {
            _searchedBusinesses.value = it
        }
    }
}

class BusinessViewModelFactory(private val repository: BusinessesRepository) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BusinessViewModel::class.java)) {
            return BusinessViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class.")
    }
}