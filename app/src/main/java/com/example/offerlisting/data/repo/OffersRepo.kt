package com.example.offerlisting.data.repo

import android.util.Log
import com.example.offerlisting.AppConstants
import com.example.offerlisting.data.datamodel.OffersDataResponse
import com.example.offerlisting.network.OfferListingService
import javax.inject.Inject

class OffersRepo @Inject constructor(
    private val offerListingService: OfferListingService
) {
    suspend fun getOffersDetails() : OffersDataResponse? {
        try {
            val response = offerListingService.getOfferList(AppConstants.OFFER_LISTING_URL)

            if(response.isSuccessful){
                return response.body()
            } else {
                Log.d("OffersRepo", "getOffersDetails: Failure")
            }
        } catch (e : Exception) {
            e.printStackTrace()
        }
        return null
    }
}