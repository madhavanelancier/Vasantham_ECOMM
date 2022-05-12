package com.elanciers.vasantham_stores_ecomm

import android.app.Activity
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.elanciers.vasantham_stores_ecomm.Adapters.YearSpinnerAdapter
import com.elanciers.vasantham_stores_ecomm.Common.Appconstands
import com.elanciers.vasantham_stores_ecomm.DataClass.YearsData
import com.elanciers.vasantham_stores_ecomm.DataClass.YearsResponse
import com.elanciers.vasantham_stores_ecomm.retrofit.ApproveUtils
import com.elanciers.vasantham_stores_ecomm.retrofit.Resp
import com.elanciers.vasantham_stores_ecomm.retrofit.RetrofitClient2
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_create_card.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateCardActivity : AppCompatActivity() {
    var tag = "card"
    lateinit var activity : Activity
    lateinit var pDialog: Dialog
    var years = ArrayList<YearsResponse>()
    var selectedYear = YearsResponse()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_card)
        activity = this
        pDialog = Dialog(this)

        imageView5.setOnClickListener {
            finish()
        }

        select_year.onItemClickListener = object : AdapterView.OnItemClickListener{
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedYear = years[p2]
                select_year.setText(years[p2].from+" - "+years[p2].to)
                select_year.setAdapter(YearSpinnerAdapter(activity,years))
            }

        }
        getyear()
    }

    fun getyear(){
        Appconstands.loading_show(activity, pDialog).show()
        val obj = JsonObject()
        obj.addProperty("type", "yeardrop")
        Log.d(tag, obj.toString())
        val call = RetrofitClient2.Get.getYears(obj)
        call.enqueue(object : Callback<YearsData> {
            override fun onResponse(call: Call<YearsData>, response: Response<YearsData>) {
                Log.e("$tag response", response.toString())
                if (response.isSuccessful()) {
                    val example = response.body() as YearsData
                    if (example.Status == "Success") {
                        years = example.Response
                        val data = Gson().toJson(example, YearsData::class.java).toString()
                        println("data : "+data)
                        select_year.setAdapter(YearSpinnerAdapter(activity,years))
                    }
                }
                pDialog.dismiss()
            }

            override fun onFailure(call: Call<YearsData>, t: Throwable) {
                Log.e("$tag Fail response", t.toString())
                if (t.toString().contains("time")) {
                    Toast.makeText(
                        activity,
                        "Poor network connection",
                        Toast.LENGTH_LONG
                    ).show()
                }
                pDialog.dismiss()
            }
        })
    }
}