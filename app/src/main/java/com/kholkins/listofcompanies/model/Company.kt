package com.kholkins.listofcompanies.model

data class Company (
    val id: String,
    val name: String,
    val img: String,
    val description: String = "",
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val www: String = "",
    val phone: String = ""
)
