package com.elanciers.vasantham_stores_ecomm

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Toast
import com.elanciers.vasantham_stores_ecomm.Adapters.AreaSpinnerAdapter
import com.elanciers.vasantham_stores_ecomm.Common.CustomLoadingDialog
import com.elanciers.vasantham_stores_ecomm.Common.Utils
import com.elanciers.vasantham_stores_ecomm.DataClass.*
import com.elanciers.vasantham_stores_ecomm.retrofit.RetrofitClient2
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_door_deivery.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DoorDeliveryActivity : AppCompatActivity() {
    var tag = "door"
    lateinit var activity : Activity
    lateinit var pDialog: CustomLoadingDialog
    var years = ArrayList<YearsResponse>()
    var Areas = ArrayList<AreaResponse>()
    var selectedArea = AreaResponse()
    lateinit var utils: Utils
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_door_deivery)
        activity = this
        utils = Utils(this)
        pDialog = CustomLoadingDialog(this)
        pDialog.setHandler(false)
        pDialog.setCancelable(false)

        //mob.setText(utils.mobile_due())
        imageView5.setOnClickListener {
            finish()
        }


        select_area.onItemClickListener = object : AdapterView.OnItemClickListener{
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedArea = Areas[p2]
                select_area.setText(Areas[p2].areaname)
                select_area.setAdapter(AreaSpinnerAdapter(activity,Areas))
                select_area.error=null
            }
        }

        submit.setOnClickListener {
            /*validatename(adrs)*/
            validatePhoneNo(mob)

        }

    }

    override fun onResume() {
        super.onResume()
        getAreas()
    }


    fun getAreas(){
        pDialog.show()
        val obj = JsonObject()
        obj.addProperty("type", "areas")
        Log.d(tag, obj.toString())
        val call = RetrofitClient2.Get.getAreas(obj)
        call.enqueue(object : Callback<AreaData> {
            override fun onResponse(call: Call<AreaData>, response: Response<AreaData>) {
                Log.e("$tag response", response.toString())
                if (response.isSuccessful()) {
                    val example = response.body() as AreaData
                    if (example.Status == "Success") {
                        Areas = example.Response!!
                        val data = Gson().toJson(example, AreaData::class.java).toString()
                        println("data : "+data)
                        select_area.setAdapter(AreaSpinnerAdapter(activity,Areas))
                    }
                }
                pDialog.dismiss()
            }

            override fun onFailure(call: Call<AreaData>, t: Throwable) {
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

    fun getCardCheck(){
        pDialog.show()
        val obj = JsonObject()
        obj.addProperty("type", "onlineCard")
        Log.d(tag, obj.toString())
        val call = RetrofitClient2.Get.CheckCard(obj)
        call.enqueue(object : Callback<CheckCardData> {
            override fun onResponse(call: Call<CheckCardData>, response: Response<CheckCardData>) {
                Log.e("$tag response", response.toString())
                if (response.isSuccessful()) {
                    val example = response.body() as CheckCardData
                    if (example.Status == "Success") {
                        val res = example.Response!!
                        val data = Gson().toJson(example, CheckCardData::class.java).toString()
                        println("data : "+data)
                    }
                }
                pDialog.dismiss()
            }

            override fun onFailure(call: Call<CheckCardData>, t: Throwable) {
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

    fun getCardCreate(){
        pDialog.show()
        /*{
    "reg_no":"1025",
    "cid":"4",
    "card_year":"2",
    "name":"Test1",
    "card_no":"20221240",
    "phone":"9786039322",
    "area":"Adalai",
    "who":"7",
    "fund1":"",
    "fund2":"",
    "gid":"",
    "collect_id":"1"
}*/
        val obj = JsonObject()
        obj.addProperty("phone", mob.text.toString())
        obj.addProperty("area", selectedArea.areaname.toString())
        obj.addProperty("gid", "")
        obj.addProperty("collect_id", "")
        Log.d(tag, obj.toString())
        val call = RetrofitClient2.Get.CreatCard(obj)
        call.enqueue(object : Callback<CreateCardData> {
            override fun onResponse(call: Call<CreateCardData>, response: Response<CreateCardData>) {
                Log.e("$tag response", response.toString())
                pDialog.dismiss()
                if (response.isSuccessful()) {
                    val example = response.body() as CreateCardData
                    if (example.Status == "Success") {
                        val data = Gson().toJson(example, CreateCardData::class.java).toString()
                        println("data : "+data)
                        finish()
                    }else{
                        Toast.makeText(activity, example.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<CreateCardData>, t: Throwable) {
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

    private fun validatePhoneNo(regPhoneNo: EditText): Boolean {
        val `val`: String = regPhoneNo.text.toString()
        try {
            //`val`.toBigInteger()
            return if (`val`.isEmpty()) {
                regPhoneNo.setError("Field cannot be empty")
                false
            } else if (`val`.length != 10) {
                regPhoneNo.setError("Enter Valid Mobile Number")
                false
            } else {
                regPhoneNo.setError(null)
                true
            }
        } catch (e: NumberFormatException) {
            return false
        }
    }

    fun validatename(regEmail: EditText, showError: Boolean = true): Boolean {
        val `val`: String = regEmail.text.toString().trim()
        return if (`val`.isEmpty()) {
            if (showError) regEmail.setError("Field cannot be empty")
            false
        } else {
            if (showError) regEmail.setError(null)
            true
        }
    }

}