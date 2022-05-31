package com.elanciers.vasantham_stores_ecomm

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
}
