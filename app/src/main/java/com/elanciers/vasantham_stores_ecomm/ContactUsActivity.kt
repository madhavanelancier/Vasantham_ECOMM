package com.elanciers.vasantham_stores_ecomm

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.elanciers.vasantham_stores_ecomm.Adapters.BranchAdapter
import com.elanciers.vasantham_stores_ecomm.Common.Appconstands
import com.elanciers.vasantham_stores_ecomm.Common.Utils
import com.elanciers.vasantham_stores_ecomm.DataClass.SettingsData
import com.elanciers.vasantham_stores_ecomm.retrofit.RetrofitClient2
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_contact_us.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactUsActivity : AppCompatActivity() {
    var tag = "Contact"
    lateinit var utils:Utils
    val activity =this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Appconstands.changeStatusBarColor(this,R.color.statusbar)
        setContentView(R.layout.activity_contact_us)
        utils = Utils(this)
        Glide.with(this).load(R.drawable.logogif).into(imageView3);
        back.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        getSetings()
    }

    fun getSetings(){
        val call = RetrofitClient2.Get.getSettings()
        call.enqueue(object : Callback<SettingsData> {
            override fun onResponse(call: Call<SettingsData>, response: Response<SettingsData>) {
                Log.e("$tag response", response.toString())
                if (response.isSuccessful()) {
                    val example = response.body() as SettingsData
                    if (example.Status == "Success") {
                        val settings = example.Response
                        utils.setSettings(settings)
                        val data = Gson().toJson(example, SettingsData::class.java).toString()
                        println("data : "+data)
                        if (settings != null) {
                            if (settings.diwaliSuprise.isNullOrEmpty()){
                                diwali_lay.visibility=View.GONE
                            }else {
                                diwali_lay.visibility=View.VISIBLE
                                diwali.setText(settings.diwaliSuprise)
                            }
                            if (settings.whatsappOrder.isNullOrEmpty()){
                                whatsapp_lay.visibility=View.GONE
                            }else {
                                whatsapp_lay.visibility=View.VISIBLE
                                whatsapp.setText(settings.whatsappOrder)
                            }
                            if (settings.customerCare.isNullOrEmpty()){
                                care_lay.visibility=View.GONE
                            }else {
                                care.setText(settings.customerCare)
                                care_lay.visibility=View.VISIBLE
                            }
                            recyclerview.adapter = BranchAdapter(this@ContactUsActivity,settings.branchList)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<SettingsData>, t: Throwable) {
                Log.e("$tag Fail response", t.toString())
                if (t.toString().contains("time")) {
                    Toast.makeText(
                        activity,
                        "Poor network connection",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })
    }

    fun openBrowser(view: View){
        val y = "http://vasanthamstore.com"
        val uri = Uri.parse(y)
        val likeIng = Intent(Intent.ACTION_VIEW, uri)
        startActivity(likeIng)
    }

    fun openDialer(view: View){
        view as TextView
        val u = Uri.parse("tel:" + view.text.toString())
        // Create the intent and set the data for the
        // intent as the phone number.
        val i = Intent(Intent.ACTION_DIAL, u)

        try {
            // Launch the Phone app's dialer with a phone
            // number to dial a call.
            startActivity(i)
        } catch (s: SecurityException) {
            // show() method display the toast with
            // exception message.
            Toast.makeText(this, "An error occurred", Toast.LENGTH_LONG)
                .show()
        }
    }
}
