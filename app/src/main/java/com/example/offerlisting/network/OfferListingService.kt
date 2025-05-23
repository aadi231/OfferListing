package com.example.offerlisting.network

import com.example.offerlisting.data.datamodel.OffersDataResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface OfferListingService {
    @GET
    suspend fun getOfferList(
        @Url offerUrl : String
    ) : Response<OffersDataResponse>
}