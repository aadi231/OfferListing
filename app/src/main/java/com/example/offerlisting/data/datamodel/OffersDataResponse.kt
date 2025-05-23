package com.example.offerlisting.data.datamodel

data class OffersDataResponse(
    val offers : List<OffersModel>,
    val categories : List<CategoryModel>
)
