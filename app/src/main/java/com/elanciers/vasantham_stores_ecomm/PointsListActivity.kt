package com.elanciers.vasantham_stores_ecomm

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.elanciers.vasantham_stores_ecomm.Adapters.CardListRecyclerAdapter
import com.elanciers.vasantham_stores_ecomm.Adapters.YearSpinnerAdapter
import com.elanciers.vasantham_stores_ecomm.Common.AppUtil
import com.elanciers.vasantham_stores_ecomm.Common.CustomLoadingDialog
import com.elanciers.vasantham_stores_ecomm.Common.Utils
import com.elanciers.vasantham_stores_ecomm.DataClass.CardList
import com.elanciers.vasantham_stores_ecomm.DataClass.CardsData
import com.elanciers.vasantham_stores_ecomm.DataClass.CardsResponse
import com.elanciers.vasantham_stores_ecomm.DataClass.YearsData
import com.elanciers.vasantham_stores_ecomm.retrofit.RetrofitClient2
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_points_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PointsListActivity : AppCompatActivity(),CardListRecyclerAdapter.OnItemClickListener {
    var tag = "pointslist"
    lateinit var activity : Activity
    lateinit var pDialog: CustomLoadingDialog
    var cards = ArrayList<CardList>()
    lateinit var utils:Utils
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_points_list)
        activity = this
        utils = Utils(this)
        pDialog = CustomLoadingDialog(this)
        pDialog.setHandler(false)
        pDialog.setCancelable(false)
        lang()
        imageView5.setOnClickListener {
            finish()
        }

    }

    override fun onResume() {
        super.onResume()
        //getCards()
    }

    fun getCards(){
        pDialog.show()
        val obj = JsonObject()
        obj.addProperty("type", "cardLists")
        obj.addProperty("phone", utils.mobile())
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
                        recyclerView.adapter=CardListRecyclerAdapter(activity,cards,this@PointsListActivity)
                        if (cards.isNotEmpty()) {
                            nodata.visibility = View.GONE
                        }else{
                            nodata.visibility = View.VISIBLE
                        }
                    }else{
                        nodata.visibility= View.VISIBLE
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
                nodata.visibility= View.VISIBLE
            }
        })
    }
    fun lang() {
        textView9.setText(AppUtil.languageString("loyalty_points"))
    }

    override fun OnItemClick(view: View, position: Int, card: CardList) {

    }
}