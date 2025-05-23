package com.example.offerlisting.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.offerlisting.data.datamodel.CategoryModel
import com.example.offerlisting.data.datamodel.OffersDataResponse
import com.example.offerlisting.data.datamodel.OffersModel
import com.example.offerlisting.data.repo.OffersRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OffersViewModel @Inject constructor(
    private val offersRepo: OffersRepo
) : ViewModel() {

    private val _offerList = MutableLiveData<List<OffersModel>?>()

    private val _categoryList = MutableLiveData<List<CategoryModel>?>()
    val categoryList : LiveData<List<CategoryModel>?> = _categoryList

    private val selectedCategories = MutableLiveData<List<String>>(emptyList())
    private val searchQuery = MutableLiveData<String>("")
    val filteredOfferList = MediatorLiveData<List<OffersModel>?>()

    init {
        filteredOfferList.addSource(_offerList) { filterOffer() }
        filteredOfferList.addSource(searchQuery) { filterOffer() }
        filteredOfferList.addSource(selectedCategories) { filterOffer() }
    }


    fun getOrderData() {
        viewModelScope.launch(Dispatchers.IO) {
            val offerResponse : OffersDataResponse? = offersRepo.getOffersDetails()
            offerResponse?.let {
                _categoryList.postValue(it.categories)
                setOfferData(it.offers)
            }
        }
    }

    private fun setOfferData(filteredOffer: List<OffersModel>) {
        _offerList.postValue(filteredOffer)
    }

    fun setSearchQuery(query : String) {
        searchQuery.value = query
    }

    fun setSelectedCategories(categoriesIdList : List<String>) {
        selectedCategories.value = categoriesIdList
    }

    fun resetFilters() {
        selectedCategories.value = emptyList()
        searchQuery.value = ""
    }

    private fun filterOffer() {
        val originalList = _offerList.value ?: return
        val categories = selectedCategories.value ?: emptyList()
        val query = searchQuery.value?.trim()?.lowercase() ?: ""

        var filteredData = originalList

        if (categories.isNotEmpty()) {
            filteredData = filteredData.filter { offer ->
                categories.contains(offer.id)
            }
        }

        if (query.isNotEmpty()) {
            filteredData = filteredData.filter { offer ->
                offer.offerName.contains(query, ignoreCase = true)
            }
        }

        filteredOfferList.value = filteredData
    }
}