package com.kholkins.listofcompanies.common

import com.kholkins.listofcompanies.retrofit.RetrofitClient
import com.kholkins.listofcompanies.companiesInterface.RetrofitServices

object Common {

    private const val BASE_URL = "https://lifehack.studio/"
    val retrofitService: RetrofitServices
        get() = RetrofitClient.getClient(BASE_URL).create(RetrofitServices::class.java)

}