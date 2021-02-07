package com.kholkins.listofcompanies.companiesInterface

import com.kholkins.listofcompanies.model.Company
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitServices {

    @GET("test_task/test.php")
    fun getCompaniesList(): Call<MutableList<Company>>

    @GET("test_task/test.php?")
    fun getCompany(@Query("id") id: String?): Call<List<Company>>

}