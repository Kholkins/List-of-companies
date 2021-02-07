package com.kholkins.listofcompanies.activities

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.kholkins.listofcompanies.R
import com.kholkins.listofcompanies.common.Common
import com.kholkins.listofcompanies.companiesInterface.RetrofitServices
import com.kholkins.listofcompanies.model.Company
import com.squareup.picasso.Picasso
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_company.*

import retrofit2.Call
import retrofit2.Callback

class CompanyActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mService: RetrofitServices
    private lateinit var dialog: AlertDialog
    private lateinit var mMap: GoogleMap
    private lateinit var mCompany: Company

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company)

        val id = intent.getStringExtra(KEY_ID)

        mService = Common.retrofitService

        dialog = SpotsDialog.Builder().setCancelable(true).setContext(this).build()
        getCompany(id)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun getCompany(id :String?) {
        dialog.show()
        mService.getCompany(id).enqueue(object : Callback<List<Company>> {
            override fun onFailure(call: Call<List<Company>>, t: Throwable) {

                nameCompanyTextView.text = "Нет данных о компании"
                Log.d("getCompanyCall","failure $t")
                    dialog.dismiss()
            }

            override fun onResponse(call: Call<List<Company>>, response: retrofit2.Response<List<Company>>) =
                if (response.isSuccessful) {
                    val companies = response.body() as List<Company>
                    mCompany = companies[0]
                    loadCompany()

                    dialog.dismiss()
                }else{
                    nameCompanyTextView.text = "Нет данных о компании"
                    val code = response.code()
                    descriptionTextView.text =  getString(R.string.code_text, code)
                    dialog.dismiss()
                }
        })
    }

    private fun loadCompany() {
        val name = mCompany.name

        val actionbar = supportActionBar
        actionbar?.title = name
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.setDisplayHomeAsUpEnabled(true)

        nameCompanyTextView.text = name
        phoneTextView.text = mCompany.phone
        wwwTextView.text = mCompany.www
        descriptionTextView.text = mCompany.description

        val companyLocation = LatLng(mCompany.lat, mCompany.lon)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(companyLocation))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12F))
        mMap.addMarker(MarkerOptions().position(companyLocation).title("Тут находится $name"))

        val url = mCompany.img
        Picasso.get()
            .load("https://lifehack.studio/test_task/$url")
            .placeholder(R.drawable.placeholder)
            .fit()
            .centerInside()
            .into(imageViewCompany)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {

        const val KEY_ID = "id"

    }
}
