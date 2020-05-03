package com.kholkins.listofcompanies.activityes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.kholkins.listofcompanies.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_company.*
import org.json.JSONException

class CompanyActivity : AppCompatActivity(), OnMapReadyCallback, VolleyCallback {

    private val volleyCallback = this
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company)

        val id = intent.getStringExtra("id")

        var queue = Volley.newRequestQueue(this)
        val url = "http://megakohz.bget.ru/test_task/test.php?id=$id"
        val request =
            JsonArrayRequest(Request.Method.GET, url, null, Response.Listener { response ->
                try {
                    val item = response.getJSONObject(0)

                    val name = item.getString("name")
                    val img = item.getString("img")
                    val description = item.getString("description")
                    val lat = item.getDouble("lat")
                    val lon = item.getDouble("lon")
                    val www = item.getString("www")
                    val phone = item.getString("phone")

                    nameCompanyTextView.text = name
                    phoneTextView.text = phone
                    wwwTextView.text = www
                    descriptionTextView.text = description

                    Picasso.get()
                        .load("http://megakohz.bget.ru/test_task/$img")
                        .placeholder(R.drawable.placeholder)
                        .fit()
                        .centerInside()
                        .into(logoImageView)

                    volleyCallback.onCallback(LatLng(lat, lon), name)

                } catch (e: JSONException) {
                    Log.e("JsonError", e.toString())
                }

            }, Response.ErrorListener { error -> Log.e("VolleyError", error.toString()) })

        queue.add(request)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    override fun onCallback(response: LatLng, name: String) {

        var companyLocation = response

        mMap.moveCamera(CameraUpdateFactory.newLatLng(companyLocation))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12F))
        mMap.addMarker(MarkerOptions().position(companyLocation).title("Location of $name"))

    }

}

interface VolleyCallback {
    fun onCallback(response: LatLng, name: String)
}