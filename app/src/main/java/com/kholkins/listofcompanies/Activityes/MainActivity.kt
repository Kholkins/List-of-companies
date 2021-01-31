package com.kholkins.listofcompanies.Activityes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.kholkins.listofcompanies.R
import com.kholkins.listofcompanies.Data.MainAdapter
import com.kholkins.listofcompanies.Model.Company
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var items = ArrayList<Company>()
        val intent = Intent(this, CompanyActivity::class.java)
        val myAdapter = MainAdapter(items, object : MainAdapter.Callback {
            override fun onItemClicked(item: Company) {

                intent.putExtra("id", item.id)
                startActivity(intent)
            }
        })
        val columnCount = resources.getInteger(R.integer.column_count)
        var layoutManager = GridLayoutManager(this, columnCount)

        var queue = Volley.newRequestQueue(this)
        val url = "http://megakohz.bget.ru/test_task/test.php"
        val request =
            JsonArrayRequest(Request.Method.GET, url, null, Response.Listener { response ->
                try {

                    for (i in 0 until response.length()) {
                        val item = response.getJSONObject(i)
                        val id = item.getString("id")
                        val name = item.getString("name")
                        val img = item.getString("img")

                        items.add(Company(id, name, img))

                    }
                    recyclerView.adapter = myAdapter
                    recyclerView.layoutManager = layoutManager
                    recyclerView.hasFixedSize()
                } catch (e: JSONException) {
                    Log.e("JsonError", e.toString())
                }

            }, Response.ErrorListener { error -> Log.e("VolleyError", error.toString()) })

        queue.add(request)
    }
}
