package com.elanciers.vasantham_stores_ecomm

import android.app.Dialog
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.elanciers.vasantham_stores_ecomm.Adapters.PaymentsListAdapter
import com.elanciers.vasantham_stores_ecomm.Common.AppUtil
import com.elanciers.vasantham_stores_ecomm.Common.Appconstands
import com.elanciers.vasantham_stores_ecomm.Common.Connection
import com.elanciers.vasantham_stores_ecomm.Common.Utils
import kotlinx.android.synthetic.main.activity_digital_card.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class DigitalCard : AppCompatActivity() {
    lateinit var utils : Utils
    val activity = this
    lateinit var pDialog: Dialog
    lateinit var ab : ActionBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_digital_card)
        utils = Utils(activity)
        val mToolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(mToolbar);
        ab = supportActionBar!!
        ab!!.setDisplayShowHomeEnabled(true)
        ab!!.setDisplayHomeAsUpEnabled(true)
        ab.setHomeAsUpIndicator(R.drawable.back_arrow)
        lang()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        //menuInflater.inflate(R.menu.profile_page, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.getItemId()
        when(id){
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        if (Appconstands.net_status(this)) {
            Dues_list().execute()
        }else{
            toast("")
        }
    }

    inner class Dues_list : AsyncTask<String?, String?, String?>() {
        override fun onPreExecute() {
            pDialog = Dialog(activity)
            Appconstands.loading_show(activity, pDialog).show()
            //   Appconstands.loading_show(activity,pDialog).show()
            //productItems!!.clear()
        }

        protected override fun doInBackground(vararg strings: String?): String? {
            var result: String? = null
            val con = Connection()
            try {
                val jobj = JSONObject()
                jobj.put("cardno",intent.getStringExtra("cardno"))
                Log.i("payment_list", Appconstands.payment_list + "    " + jobj.toString())
                result = con.sendHttpPostjson(Appconstands.payment_list, jobj)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return result
        }

        override fun onPostExecute(resp: String?) {
            Log.e("resp",resp.toString())
            try {
                if (resp != null) {

                    val jobj = JSONObject(resp)
                    if (jobj.getString("Status") == "Success") {
                        val jobject=jobj.getJSONArray("Response")
                        val ob = jobject.getJSONObject(0)
                        val card_year=jobject.getJSONObject(0).getString("card_year")
                        val name=jobject.getJSONObject(0).getString("name")
                        val chit_name=jobject.getJSONObject(0).getString("chit_name")
                        val fund_1=if (ob.getString("fund1")!="null")ob.getString("fund1") else ""
                        val fund_2=if (ob.getString("fund2")!="null")ob.getString("fund2") else ""
                        val amount=ob.getString("amount")
                        val qrimage=jobject.getJSONObject(0).getString("qrimage")
                        println("qrimage : "+qrimage)
                        ab.title = name
                        chit.setText(chit_name)
                        fund1.setText(fund_1)
                        fund2.setText(fund_2)
                        amnt.setText(amount)
                        payments.adapter= PaymentsListAdapter(activity,jobject.getJSONObject(0).getJSONArray("payment"))
                        pDialog.dismiss()
                        if (qrimage.isNotEmpty()){
                            val decodedBytes = android.util.Base64.decode(qrimage,android.util.Base64.DEFAULT)
                            val decodedString = String(decodedBytes)
                            println("image : "+decodedString)
                            Glide.with(activity).load(decodedString).into(imageView35)
                            imageView35.visibility= View.VISIBLE
                        }else{
                            imageView35.visibility= View.GONE
                        }

                    } else {
                        toast(jobj.getString("message"))
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

    fun lang(){
        chit_.setHint(AppUtil.languageString("chit"))
        chit.setHint(AppUtil.languageString("chit"))
        fund1_.setHint(AppUtil.languageString("fund1"))
        fund1.setHint(AppUtil.languageString("fund1"))
        fund2_.setHint(AppUtil.languageString("fund2"))
        fund2.setHint(AppUtil.languageString("fund2"))
        amnt_.setHint(AppUtil.languageString("amount"))
        amnt.setHint(AppUtil.languageString("amount"))

        textView50.setText(AppUtil.languageString("s_no"))
        textView55.setText(AppUtil.languageString("month"))
        textView56.setText(AppUtil.languageString("date_amp_time"))
        textView57.setText(AppUtil.languageString("rec_no"))
        textView58.setText(AppUtil.languageString("amount"))
    }
}