package com.kholkins.listofcompanies.activities

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.kholkins.listofcompanies.common.Common
import com.kholkins.listofcompanies.companiesInterface.RetrofitServices
import com.kholkins.listofcompanies.R
import com.kholkins.listofcompanies.data.CompaniesAdapter
import com.kholkins.listofcompanies.model.Company
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var mService: RetrofitServices
    private var columnCount: Int = 1
    private lateinit var layoutManager: GridLayoutManager
    private lateinit var myAdapter: CompaniesAdapter
    private lateinit var dialog: AlertDialog
    private lateinit var companies: MutableList<Company>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mService = Common.retrofitService

        columnCount = resources.getInteger(R.integer.column_count)
        layoutManager = GridLayoutManager(this, columnCount)

        dialog = SpotsDialog.Builder().setCancelable(true).setContext(this).build()

        getAllCompaniesList()
    }

    private fun getAllCompaniesList() {
        dialog.show()
        mService.getCompaniesList().enqueue(object : Callback<MutableList<Company>> {
            override fun onFailure(call: Call<MutableList<Company>>, t: Throwable) {

            }

            override fun onResponse(call: Call<MutableList<Company>>, response: Response<MutableList<Company>>) {
                companies = response.body() as MutableList<Company>
                myAdapter = CompaniesAdapter(companies, object : CompaniesAdapter.Callback {
                    override fun onItemClicked(item: Company) {
                        val intent = Intent(this@MainActivity, CompanyActivity::class.java)
                        val id = item.id
                        intent.putExtra(CompanyActivity.KEY_ID, id)
                        startActivity(intent)
                    }
                })
                recyclerViewCompanies.adapter = myAdapter
                recyclerViewCompanies.layoutManager = layoutManager
                recyclerViewCompanies.setHasFixedSize(true)
                myAdapter.notifyDataSetChanged()
                dialog.dismiss()
            }
        })
    }


}
