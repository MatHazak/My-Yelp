package me.mathazak.myyelp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.mathazak.myyelp.data.BusinessesRepository
import me.mathazak.myyelp.data.YelpBusiness

class BusinessViewModel(private val repository: BusinessesRepository) : ViewModel() {
    val likedBusinesses = repository.likedBusinesses.asLiveData()
    val searchedBusinesses = repository.searchedBusinesses

    fun insert(business: YelpBusiness) = viewModelScope.launch {
        repository.insert(business)
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