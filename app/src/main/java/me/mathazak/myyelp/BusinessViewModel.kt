package me.mathazak.myyelp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.mathazak.myyelp.data.BusinessesRepository
import me.mathazak.myyelp.data.YelpBusiness
import me.mathazak.myyelp.data.YelpSearchRequest

class BusinessViewModel(private val repository: BusinessesRepository) : ViewModel() {
    val favoriteBusinesses = repository.favoriteBusinesses.asLiveData()
    val searchedBusinesses = repository.searchedBusinesses

    fun insert(business: YelpBusiness) = viewModelScope.launch {
        repository.insert(business)
    }

    fun delete(business: YelpBusiness) = viewModelScope.launch {
        repository.delete(business)
    }

    fun fetchNewSearch(yelpSearchRequest: YelpSearchRequest) = viewModelScope.launch {
        repository.searchBusinesses(yelpSearchRequest)
    }
}

class BusinessViewModelFactory(private val repository: BusinessesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BusinessViewModel::class.java)) {
            return BusinessViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class.")
    }
}