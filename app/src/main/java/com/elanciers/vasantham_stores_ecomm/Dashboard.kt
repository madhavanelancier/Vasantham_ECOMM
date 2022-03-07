package com.elanciers.vasantham_stores_ecomm

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.elancier.vasantham_stores.Adapters.Due_list
import com.elanciers.vasantham_stores_ecomm.Common.Appconstands
import com.elanciers.vasantham_stores_ecomm.Common.Connection
import com.elanciers.vasantham_stores_ecomm.Common.Utils
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.android.synthetic.main.activity_dashboard.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class Dashboard : AppCompatActivity(), PaymentResultListener {
    internal lateinit var itemsAdapter: Due_list
    val activity = this
    lateinit var utils : Utils
    lateinit var pDialog: Dialog
    var tid = "";
    var total = "";
    var receipt = "";
    var due = "";
    var branch = "";
    var lat = "";
    var lng = "";

    private val mRecyclerListitems = ArrayList<Any>()
    private var productItems: MutableList<Rewardpointsbo>? = null

    internal lateinit var mLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        productItems = ArrayList()
        utils = Utils(activity)
        val cardno = intent.getStringExtra("cardno")
        val btn = intent.getStringExtra("btn")

        if(btn=="1"){
            pay.visibility=View.VISIBLE
        }
        else{
            pay.visibility=View.GONE

        }

        recyclerView.setLayoutManager(
            LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        )

        itemsAdapter = Due_list(mRecyclerListitems, this, object :
            Due_list.OnItemClickListener {
            override fun OnItemClick(view: View, position: Int, viewType: Int) {
                val item = mRecyclerListitems[position] as Rewardpointsbo
                // Log.e("clickresp", "value")

                //clikffed();
            }
        })
        recyclerView.adapter = itemsAdapter


        pay.setOnClickListener {
            pay.isEnabled=false
            startpayment()
        }

        imageView6.setOnClickListener {
            startActivity(Intent(activity, HomeActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            finish()

        }
        textView49.setOnClickListener {
            val st = Intent(activity, DigitalCard::class.java)
            st.putExtra("cardno",cardno)
            startActivity(st)
        }
        track.setOnClickListener {
            val st = Intent(activity, LocationActivity::class.java)
            st.putExtra("lat",lat)
            st.putExtra("lng",lng)
            startActivity(st)
        }

    }

    fun startpayment() {
        val checkout = Checkout()
        try {
            val total = pending_amt.text.toString().toDouble()
            val options = JSONObject()
            options.put("name", "Vasantham Stores")
            options.put("description", cardnum.text.toString())
            options.put("image","https://elancier.xyz/vasantham_stores/assets/images/logo.png")
            options.put("currency", "INR")
            val st =
                StringTokenizer(total.toString().trim({ it <= ' ' }), ".")
            val value1 = st.nextToken()
            val value2 = st.nextToken()
            println("st : " + st)
            println("value2 : " + value2)
            val paymentamount = Integer.valueOf(value2)
            println("paymentamount : " + (paymentamount * 100))
            options.put("amount", total * 100)//paymentamount * 100
            val preFill = JSONObject()
            preFill.put("email", "")
            preFill.put("contact", utils.mobile_due())
            options.put("prefill", preFill)
            pDialog.dismiss()
            checkout.open(activity, options)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(razorpayPaymentID: String?) {
        try {
            pay.isEnabled=true

            Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show()
            Log.e("razorpayPaymentID", razorpayPaymentID!!)
            tid=razorpayPaymentID.toString()
            Handler().postDelayed(Runnable {
                val payment = OrderSend()
                payment.execute(tid, "razorpay", "success")
            }, 2000)

        } catch (e: java.lang.Exception) {
            Log.e(
                "dvsf",
                "Exception in onPaymentSuccess",
                e
            )
        }
    }

    override fun onPaymentError(code: Int, response: String) {
        try {
            pay.isEnabled=true

            Toast.makeText(this, "Payment failed: $code $response", Toast.LENGTH_LONG).show()
           /* val payment = PaymentStatus()
            payment.execute("", response)*/
            /*if (dbCon.getOverAll() === 0) {
                //startActivity(Intent(this@CartActivity, JewelHome::class.java))
            } else {
            }*/
        } catch (e: java.lang.Exception) {
            Log.e(
                "fvf",
                "Exception in onPaymentError",
                e
            )
        }
    }

    private inner class OrderSend : AsyncTask<String, Void, String>() {

        override fun onPreExecute() {
            pDialog = Dialog(activity)
            Appconstands.loading_show(activity, pDialog).show()
        }

        override fun doInBackground(vararg param: String): String? {
            var result: String = ""

            try {
                val jobj = JSONObject()
                jobj.put("cus_id", utils.due_id())
                jobj.put("amount", pending_amt.text.toString().trim())
                jobj.put("paytype", "online")
                jobj.put("branch", branch)
                jobj.put("tid", tid)


                Log.i("payment Input", Appconstands.order1 + "    " + jobj.toString())
                result = Connection().sendHttpPostjson(Appconstands.order1, jobj)


            } catch (e: Exception) {
                e.printStackTrace()
            }

            return result
        }

        override fun onPostExecute(resp: String?) {
            try {
                if (!resp.isNullOrEmpty()) {
                    Log.e("newresp", resp)
                    val obj1 = JSONArray(resp)
                    if (obj1.getJSONObject(0).getString("Status") == "Success") {
                        pDialog.dismiss()
                        val cal=Calendar.getInstance()
                        val format=SimpleDateFormat("dd-MMM-YY hh:mm aa")
                        val time=format.format(cal.time)
                        println("time"+time)

                        if(due=="0"){
                            due="1"
                        }
                        else if(due=="12"){
                            due="Fully Paid"
                        }


                        toast(obj1.getJSONObject(0).getString("Response"))
                        startActivity(Intent(this@Dashboard,Payment_Reciept::class.java)
                            .putExtra("name",nm.text.toString().trim())
                            .putExtra("mobile",utils.mobile_due())
                            .putExtra("card",cardnum.text.toString())
                            .putExtra("amout",pending_amt.text.toString())
                            .putExtra("rec",obj1.getJSONObject(0).getString("receptno"))
                            .putExtra("due",due)
                            .putExtra("date",time))


                    } else {
                        pDialog.dismiss()
                        toast(obj1.getJSONObject(0).getString("Response"))
                    }
                } else {
                    pDialog.dismiss()
                }
            } catch (e: Exception) {
                pDialog.dismiss()
                Log.e("err",e.toString())
            }

        }
    }


    override fun onResume() {
        super.onResume()
        pDialog = Dialog(activity)

        Dues().execute()
    }

    inner class Dues : AsyncTask<String?, String?, String?>() {
        override fun onPreExecute() {
            Appconstands.loading_show(activity,pDialog).show()
        }

        protected override fun doInBackground(vararg strings: String?): String? {
            var result: String? = null
            val con = Connection()
            try {
                val jobj = JSONObject()
                jobj.put("userid",utils.due_id())
                Log.i("rewardinputDues", Appconstands.payment + "    " + jobj.toString())
                result = con.sendHttpPostjson(Appconstands.payment, jobj)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return result
        }

        override fun onPostExecute(resp: String?) {

            try {
                Log.e("resp",resp.toString())
                if (resp != null) {

                    val jobj = JSONArray(resp)
                    if (jobj.getJSONObject(0).getString("Status") == "Success") {

                        val jobject=jobj.getJSONObject(0).getJSONObject("Response")
                        cardnum.setText(jobject.getString("cardno"))
                        nm.setText(jobject.getString("name"))
                        ltype.setText(jobject.getString("loanType"))
                        pending_amt.setText(jobject.getString("instalment"))
                        p_dues.setText(jobject.getString("totalDue"))
                        //receipt=jobject.getString("receptno")
                        due=jobject.getString("paidDue")
                        branch=jobject.getString("branch")
                        cardnum.setFocusable(false)
                        nm.setFocusable(false)
                        ltype.setFocusable(false)
                        pending_amt.setFocusable(false)

                        p_dues.setFocusable(false)

                        if(p_dues.text.toString()=="0"){
                            pay.visibility=View.INVISIBLE
                        }

                        textView44.setText(utils.name_due())
                        textView46.setText(jobject.getString("cardno"))
                       // pDialog.dismiss()
                        Dues_list().execute()


                    } else {
                        toast(jobj.getJSONObject(0).getString("Response"))

                        //pDialog.dismiss()
                        Dues_list().execute()


                    }
                }
            }catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }



    inner class Dues_list : AsyncTask<String?, String?, String?>() {
        override fun onPreExecute() {
         //   Appconstands.loading_show(activity,pDialog).show()
            productItems!!.clear()
        }

        protected override fun doInBackground(vararg strings: String?): String? {
            var result: String? = null
            val con = Connection()
            try {
                val jobj = JSONObject()
                jobj.put("userid",utils.due_id())
                Log.i("rewardinput", Appconstands.paymentlist + "    " + jobj.toString())
                result = con.sendHttpPostjson(Appconstands.paymentlist, jobj)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return result
        }

        override fun onPostExecute(resp: String?) {
        Log.e("resp",resp.toString())
            try {
                if (resp != null) {

                    val jobj = JSONArray(resp)
                    if (jobj.getJSONObject(0).getString("Status") == "Success") {

                        val jobject=jobj.getJSONObject(0).getJSONArray("Response")
                        val qrimage=jobj.getJSONObject(0).getString("qrimage")
                        lat=jobj.getJSONObject(0).getString("lat")
                        lng=jobj.getJSONObject(0).getString("lng")
                        println("qrimage : "+qrimage)

                        for(i in 0 until jobject.length()){
                            val cardno=(jobject.getJSONObject(i).getString("card_no"))
                            val payid=(jobject.getJSONObject(i).getString("payid"))
                            val receptno=(jobject.getJSONObject(i).getString("receptno"))
                            val amount=(jobject.getJSONObject(i).getString("amount"))
                            val pay_date=(jobject.getJSONObject(i).getString("pay_date"))
                            val due=(jobject.getJSONObject(i).getString("due"))

                            val frmdate = amount
                            val todate = "true"
                            val feedback_absent = cardno
                            val frombank=  pay_date
                            val tid=  receptno
                            val vdtime= ""
                            val reject= ""
                            //val feedback_absents=JO.getString("dtime")

                            try {
                                productItems!!.add(
                                    Rewardpointsbo(
                                        due,
                                        frmdate,
                                        todate,
                                        feedback_absent,
                                        frombank,
                                        vdtime,
                                        tid,
                                        "",
                                        reject,
                                        ""
                                    )
                                )
                            } catch (e: Exception) {
                                productItems!!.add(
                                    Rewardpointsbo(
                                        due,
                                        frmdate,
                                        todate,
                                        feedback_absent,
                                        frombank,
                                        vdtime,
                                        tid,
                                        "",
                                        reject,
                                        ""
                                    )
                                )
                            }
                        }

                        mRecyclerListitems.clear()
                        mRecyclerListitems.addAll(productItems!!)
                        itemsAdapter.notifyDataSetChanged()

                        pDialog.dismiss()
                        if (qrimage.isNotEmpty()){
                            scroll.visibility=View.GONE
                            imageView35.visibility=View.VISIBLE
                            val decodedBytes = android.util.Base64.decode(qrimage,android.util.Base64.DEFAULT)
                            val decodedString = String(decodedBytes)
                            println("image : "+decodedString)
                            Glide.with(this@Dashboard).load(decodedString).into(imageView35)
                            if (lat.isNotEmpty()&& lng.isNotEmpty()){
                                track.visibility=View.VISIBLE
                            }else{
                                track.visibility=View.GONE
                            }
                        }else{
                            scroll.visibility=View.VISIBLE
                            imageView35.visibility=View.GONE
                            track.visibility=View.GONE
                        }

                    } else {
                        toast(jobj.getJSONObject(0).getString("Response"))
                        pDialog.dismiss()

                    }
                }
            }catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    fun toast(msg:String){
        val toast= Toast.makeText(this,msg, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER,0,0)
        toast.show()
    }

    fun bg(pos:Int){

       /* val size=productItems!!.size

        for(i in 0 until size) {

            if(pos==i) {
                val frmdate = productItems!!.get(pos).date
                val todate = "true"
                val feedback_absent = productItems!!.get(pos).username
                val frombank = ""
                val tid = ""
                val vdtime = ""
                val reject = ""
                //val feedback_absents=JO.getString("dtime")
                try {
                    productItems!!.set(
                        pos,
                        Rewardpointsbo(
                            pos.toString(),
                            frmdate,
                            todate,
                            feedback_absent,
                            frombank,
                            vdtime,
                            tid,
                            "",
                            reject,
                            ""
                        )
                    )
                } catch (e: Exception) {
                    productItems!!.set(
                        pos,
                        Rewardpointsbo(
                            pos.toString(),
                            frmdate,
                            todate,
                            feedback_absent,
                            frombank,
                            vdtime,
                            tid,
                            "",
                            reject,
                            ""
                        )
                    )
                }
            }
            else{
                val frmdate = productItems!!.get(pos).date
                val todate = "false"
                val feedback_absent = productItems!!.get(pos).username
                val frombank = ""
                val tid = ""
                val vdtime = ""
                val reject = ""
                //val feedback_absents=JO.getString("dtime")
                try {
                    productItems!!.set(
                        i,
                        Rewardpointsbo(
                            i.toString(),
                            frmdate,
                            todate,
                            feedback_absent,
                            frombank,
                            vdtime,
                            tid,
                            "",
                            reject,
                            ""
                        )
                    )
                } catch (e: Exception) {
                    productItems!!.set(
                        i,
                        Rewardpointsbo(
                            i.toString(),
                            frmdate,
                            todate,
                            feedback_absent,
                            frombank,
                            vdtime,
                            tid,
                            "",
                            reject,
                            ""
                        )
                    )
                }
            }

        }



        mRecyclerListitems.clear()
        mRecyclerListitems.addAll(productItems!!)
        itemsAdapter.notifyDataSetChanged()*/
    }
}