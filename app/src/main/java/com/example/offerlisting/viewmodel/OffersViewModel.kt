package com.example.offerlisting.viewmodel

import android.util.Log
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

    private val selectedCategories = MutableLiveData<Set<String>>(emptySet())
    private val searchQuery = MutableLiveData<String>("")
    val filteredOfferList = MediatorLiveData<List<OffersModel>?>()
    val selectedCategoryList = MediatorLiveData<List<CategoryModel>?>()

    private var selectedIds = mutableSetOf<String>()

    init {
        filteredOfferList.addSource(_offerList) { filterOffer() }
        filteredOfferList.addSource(searchQuery) { filterOffer() }
        filteredOfferList.addSource(selectedCategories) { filterOffer() }

        selectedCategoryList.addSource(selectedCategories) { updateCategoriesSelection() }
        selectedCategoryList.addSource(_categoryList) { updateCategoriesSelection() }
    }

    fun getOfferData() {
        viewModelScope.launch(Dispatchers.IO) {
            val offerResponse : OffersDataResponse? = offersRepo.getOffersDetails()
            offerResponse?.let {
                _categoryList.postValue(it.categories)
                _offerList.postValue(it.offers)
            }
        }
    }

    fun setSearchQuery(query : String) {
        searchQuery.value = query
    }

    fun setSelectedCategories() {
        Log.d("ViewModel", "setSelectedCategories: ${selectedIds.toString()}")
        selectedCategories.value = selectedIds.toSet()
    }

    private fun updateCategoriesSelection() {
        viewModelScope.launch(Dispatchers.Default) {
            Log.d("ViewModel", "updateCategoriesSelection: ${selectedCategories.value.toString()}")
            val updatedCategories = _categoryList.value?.map { category ->
                category.copy(
                    isSelected = selectedCategories.value?.contains(category.id) == true
                )
            }
            updatedCategories?.let {
                selectedCategoryList.postValue(it)
            }
        }
    }

    fun updateSelectedIdList(id : String, checked : Boolean) {
        if (checked) {
            selectedIds.add(id)
        } else {
            selectedIds.remove(id)
        }
    }

    fun resetFilters() {
        selectedIds.clear()
        selectedCategories.value = emptySet()
    }

    private fun filterOffer() {
        viewModelScope.launch(Dispatchers.Default) {
            Log.d("ViewModel", "filterOffer: ${selectedCategories.value.toString()}")
            val originalList = _offerList.value ?: return@launch
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
            filteredOfferList.postValue(filteredData)
        }
    }
}