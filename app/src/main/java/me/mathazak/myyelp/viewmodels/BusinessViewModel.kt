package me.mathazak.myyelp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.mathazak.myyelp.data.Business
import me.mathazak.myyelp.data.BusinessesRepository
import me.mathazak.myyelp.utils.Constants.DEFAULT_SEARCH_TERM
import me.mathazak.myyelp.utils.DataStatus
import javax.inject.Inject


@HiltViewModel
class BusinessViewModel @Inject constructor(private val repository: BusinessesRepository) :
    ViewModel() {

    var searchTerm = DEFAULT_SEARCH_TERM

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
        repository.searchBusinesses(searchTerm).collect {
            _searchedBusinesses.value = it
        }
    }
}