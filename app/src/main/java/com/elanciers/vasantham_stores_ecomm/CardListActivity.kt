package com.elanciers.vasantham_stores_ecomm

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.elanciers.vasantham_stores_ecomm.Adapters.CardListRecyclerAdapter
import com.elanciers.vasantham_stores_ecomm.Adapters.YearSpinnerAdapter
import com.elanciers.vasantham_stores_ecomm.Common.CustomLoadingDialog
import com.elanciers.vasantham_stores_ecomm.Common.Utils
import com.elanciers.vasantham_stores_ecomm.DataClass.CardList
import com.elanciers.vasantham_stores_ecomm.DataClass.CardsData
import com.elanciers.vasantham_stores_ecomm.DataClass.CardsResponse
import com.elanciers.vasantham_stores_ecomm.DataClass.YearsData
import com.elanciers.vasantham_stores_ecomm.retrofit.RetrofitClient2
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_card_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CardListActivity : AppCompatActivity() {
    var tag = "cardlist"
    lateinit var activity : Activity
    lateinit var pDialog: CustomLoadingDialog
    var cards = ArrayList<CardList>()
    lateinit var utils:Utils
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_list)
        activity = this
        utils = Utils(this)
        pDialog = CustomLoadingDialog(this)
        pDialog.setHandler(false)
        pDialog.setCancelable(false)

        imageView5.setOnClickListener {
            finish()
        }
        create.setOnClickListener {
            val st = Intent(this,CreateCardActivity::class.java)
            startActivity(st)
        }
    }

    override fun onResume() {
        super.onResume()
        getCards()
    }

    fun getCards(){
        pDialog.show()
        val obj = JsonObject()
        obj.addProperty("type", "cardLists")
        obj.addProperty("phone", utils.mobile_due())
        Log.d(tag, obj.toString())
        val call = RetrofitClient2.Get.getCards(obj)
        call.enqueue(object : Callback<CardsData> {
            override fun onResponse(call: Call<CardsData>, response: Response<CardsData>) {
                Log.e("$tag response", response.toString())
                if (response.isSuccessful()) {
                    val example = response.body() as CardsData
                    if (example.Status == "Success") {
                        cards = example.Response.cardList
                        val data = Gson().toJson(example, CardsData::class.java).toString()
                        println("data : "+data)
                        recyclerView.adapter=CardListRecyclerAdapter(activity,cards)
                    }
                }
                pDialog.dismiss()
            }

            override fun onFailure(call: Call<CardsData>, t: Throwable) {
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