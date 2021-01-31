package com.kholkins.listofcompanies.CompaniesInterface

import com.kholkins.listofcompanies.Model.Company
import retrofit2.Call
import retrofit2.http.GET

interface RetrofitServieces {

    @GET("test_task/test.php")
    fun getCompaniesList(): Call<MutableList<Company>>

}