package com.elanciers.vasantham_stores_ecomm

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.elanciers.vasantham_stores_ecomm.Adapters.CardListRecyclerAdapter
import com.elanciers.vasantham_stores_ecomm.Adapters.PointsListRecyclerAdapter
import com.elanciers.vasantham_stores_ecomm.Adapters.YearSpinnerAdapter
import com.elanciers.vasantham_stores_ecomm.Common.AppUtil
import com.elanciers.vasantham_stores_ecomm.Common.CustomLoadingDialog
import com.elanciers.vasantham_stores_ecomm.Common.Utils
import com.elanciers.vasantham_stores_ecomm.DataClass.*
import com.elanciers.vasantham_stores_ecomm.retrofit.RetrofitClient2
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_points_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PointsListActivity : AppCompatActivity() {
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
        getPoints()
    }

    fun getPoints(){
        pDialog.show()
        val obj = JsonObject()
        obj.addProperty("mobile", utils.mobile())
        Log.d(tag, obj.toString())
        val call = RetrofitClient2.Get.getLoyaltypointsList("http://vasanthamhypermart.in/api/salesbycustomer",obj)
        call.enqueue(object : Callback<LoyaltyList> {
            override fun onResponse(call: Call<LoyaltyList>, response: Response<LoyaltyList>) {
                Log.e("$tag response", response.toString())
                if (response.isSuccessful()) {
                    val example = response.body() as LoyaltyList
                    val data = Gson().toJson(example, LoyaltyList::class.java).toString()
                    println("data : "+data)
                    if (example.success!=null) {
                        example.success?.let {
                            if (it.orders.isEmpty()){
                                nodata.visibility=View.VISIBLE
                            }else {
                                nodata.visibility = View.GONE
                                recyclerView.adapter =
                                    PointsListRecyclerAdapter(activity, it.orders)
                            }
                        }
                    }else{
                        nodata.visibility=View.VISIBLE
                    }
                }else{
                    nodata.visibility=View.VISIBLE
                }
                pDialog.dismiss()
            }

            override fun onFailure(call: Call<LoyaltyList>, t: Throwable) {
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
        date.setText(AppUtil.languageString("date"))
        billamnt.setText(AppUtil.languageString("billamnt"))
        pointsearned.setText(AppUtil.languageString("pointsearned"))
    }
}