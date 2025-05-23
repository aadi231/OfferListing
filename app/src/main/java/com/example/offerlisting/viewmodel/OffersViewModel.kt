package com.example.offerlisting.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
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
    val offerList : LiveData<List<OffersModel>?> = _offerList

    private val _categoryList = MutableLiveData<List<CategoryModel>?>()
    val categoryList : LiveData<List<CategoryModel>?> = _categoryList


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

    fun filterOffer(categoryIdList : List<String>) {
        val filteredOffer = _offerList.value?.filter { offer ->
            categoryIdList.any { id -> id == offer.id }
        }
        Log.d("ViewModel", "filterOffer: ${filteredOffer.toString()}")
        filteredOffer?.let { setOfferData(it) }
    }
}