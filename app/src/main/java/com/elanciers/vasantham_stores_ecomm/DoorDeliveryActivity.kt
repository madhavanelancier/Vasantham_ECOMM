package com.elanciers.vasantham_stores_ecomm

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Toast
import com.elanciers.vasantham_stores_ecomm.Adapters.AreaSpinnerAdapter
import com.elanciers.vasantham_stores_ecomm.Common.AppUtil
import com.elanciers.vasantham_stores_ecomm.Common.CustomLoadingDialog
import com.elanciers.vasantham_stores_ecomm.Common.Utils
import com.elanciers.vasantham_stores_ecomm.DataClass.*
import com.elanciers.vasantham_stores_ecomm.retrofit.RetrofitClient2
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_door_deivery.*
import kotlinx.android.synthetic.main.activity_door_deivery.imageView5
import kotlinx.android.synthetic.main.activity_door_deivery.textView9
import kotlinx.android.synthetic.main.activity_doordelivery_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DoorDeliveryActivity : AppCompatActivity() {
    var tag = "door"
    lateinit var activity : Activity
    lateinit var pDialog: CustomLoadingDialog
    var years = ArrayList<YearsResponse>()
    var Areas = ArrayList<AreaResponse>()
    var selectedArea = AreaResponse()
    lateinit var utils: Utils
    var customer = CustomerResponse()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_door_deivery)
        activity = this
        utils = Utils(this)
        pDialog = CustomLoadingDialog(this)
        pDialog.setHandler(false)
        pDialog.setCancelable(false)
        lang()
        date.setText(SimpleDateFormat("dd-MM-yyyy").format(Date()).toString())
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

        card_number.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (card_number.text.toString().length==8){
                    getCustomer()
                }else{
                    customer = CustomerResponse()
                    setCustomerdata()
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        submit.setOnClickListener {
            /*validatename(adrs)*/
            validatename(adrs1)
            validatename(adrs2)
            validatename(landmark)
            validatename(pincode)
            validatename(card_number)
            validatePhoneNo(mob)
            if (validatename(adrs1)&&validatename(adrs2)&&validatename(landmark)&&validatename(pincode)&&validatename(card_number)&&validatePhoneNo(mob)){
                if (select_area.text.toString()!="Select") {
                    saveDelivery()
                }else{
                    select_area.setError("Select Area")
                }
            }
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

    fun getCustomer(){
        pDialog.show()
        val obj = JsonObject()
        obj.addProperty("type", "customerDetails")
        obj.addProperty("card_no", card_number.text.toString())
        /*{
    "type":"customerDetails",
    "card_no":"20220003"
}*/
        Log.d(tag, obj.toString())
        val call = RetrofitClient2.Get.getCustomer(obj)
        call.enqueue(object : Callback<CustomerData> {
            override fun onResponse(call: Call<CustomerData>, response: Response<CustomerData>) {
                Log.e("$tag response", response.toString())
                if (response.isSuccessful()) {
                    val example = response.body() as CustomerData
                    if (example.Status == "Success") {
                        customer = example.Response!!
                        val data = Gson().toJson(example, CustomerData::class.java).toString()
                        println("data : "+data)
                        setCustomerdata()
                    }else{
                        customer = CustomerResponse()
                        setCustomerdata()
                    }
                }else{
                    customer = CustomerResponse()
                    setCustomerdata()
                }
                pDialog.dismiss()
            }

            override fun onFailure(call: Call<CustomerData>, t: Throwable) {
                Log.e("$tag Fail response", t.toString())
                if (t.toString().contains("time")) {
                    Toast.makeText(
                        activity,
                        "Poor network connection",
                        Toast.LENGTH_LONG
                    ).show()
                }
                pDialog.dismiss()
                customer = CustomerResponse()
                setCustomerdata()
            }
        })
    }

    fun setCustomerdata(){
        cust_name.setText(customer.name)
        cust_phone.setText(customer.phone)
        del_amount.setText(customer.deliveryAmt)
        select_area.setText(customer.area)
        loantype.setText(customer.loanType)
        select_area.setAdapter(AreaSpinnerAdapter(activity,Areas))
    }

    fun saveDelivery(){
        pDialog.show()
        /*{
    "cus_id":"",
    "card_year":"",
    "card_no":"",
    "delivery_amt":"",
    "address_1":"",
    "address_2":"",
    "dpincode":"",
    "landmark":"",
    "aphone":"",
    "area":""

}*/
        val obj = JsonObject()
        obj.addProperty("cus_id", customer.id)
        obj.addProperty("card_year", customer.cardYear)
        obj.addProperty("card_no", customer.cardNo)
        obj.addProperty("delivery_amt", customer.deliveryAmt)
        obj.addProperty("address_1", adrs1.text.toString().trim())
        obj.addProperty("address_2", adrs2.text.toString().trim())
        obj.addProperty("dpincode", pincode.text.toString().trim())
        obj.addProperty("landmark", landmark.text.toString().trim())
        obj.addProperty("aphone", mob.text.toString().trim())
        obj.addProperty("area", select_area.text.toString().trim())
        Log.d(tag, obj.toString())
        val call = RetrofitClient2.Get.CreateDelivery(obj)
        call.enqueue(object : Callback<CreateDeliveryData> {
            override fun onResponse(call: Call<CreateDeliveryData>, response: Response<CreateDeliveryData>) {
                Log.e("$tag response", response.toString())
                pDialog.dismiss()
                if (response.isSuccessful()) {
                    val example = response.body() as CreateDeliveryData
                    if (example.Status == "Success") {
                        /*val data = Gson().toJson(example, CreateDeliveryData::class.java).toString()
                        println("data : "+data)*/
                        finish()
                    }else{
                        Toast.makeText(activity, example.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<CreateDeliveryData>, t: Throwable) {
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
    fun lang() {
        textView9.setText(AppUtil.languageString("door_delivery"))
        dat.setHint(AppUtil.languageString("date"))
        //date.setHint(AppUtil.languageString("date"))
        card_no.setHint(AppUtil.languageString("card_number"))
        //card_number.setHint(AppUtil.languageString("card_number"))
        ltype.setHint(AppUtil.languageString("loantype"))
        //loantype.setHint(AppUtil.languageString("loantype"))
        custnm.setHint(AppUtil.languageString("custmer_name"))
        //cust_name.setHint(AppUtil.languageString("custmer_name"))
        custph.setHint(AppUtil.languageString("customer_phone"))
        //cust_phone.setHint(AppUtil.languageString("customer_phone"))
        amob.setHint(AppUtil.languageString("alt_mobile_number"))
        //mob.setHint(AppUtil.languageString("alt_mobile_number"))
        delamount.setHint(AppUtil.languageString("delivery_amount"))
        //del_amount.setHint(AppUtil.languageString("delivery_amount"))
        adrs_1.setHint(AppUtil.languageString("address_line1"))
        //adrs1.setHint(AppUtil.languageString("address_line1"))
        adrs_2.setHint(AppUtil.languageString("address_line2"))
        //adrs2.setHint(AppUtil.languageString("address_line2"))
        land.setHint(AppUtil.languageString("landmark"))
        //landmark.setHint(AppUtil.languageString("landmark"))
        pin.setHint(AppUtil.languageString("pincode"))
        //pincode.setHint(AppUtil.languageString("pincode"))
        area.setHint(AppUtil.languageString("select_area"))
        submit.setText(AppUtil.languageString("submit"))
    }
}