package com.elanciers.vasantham_stores_ecomm

import android.app.Dialog
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.elanciers.vasantham_stores_ecomm.Adapters.CardHistoyRecyclerAdapter
import com.elanciers.vasantham_stores_ecomm.Common.AppUtil
import com.elanciers.vasantham_stores_ecomm.Common.Appconstands
import com.elanciers.vasantham_stores_ecomm.Common.Appconstands.loading_show
import com.elanciers.vasantham_stores_ecomm.Common.Connection
import com.elanciers.vasantham_stores_ecomm.Common.Utils
import com.elanciers.vasantham_stores_ecomm.Database.CardHistoryData
import com.elanciers.vasantham_stores_ecomm.Database.CardHistoryDatabase
import kotlinx.android.synthetic.main.activity_due_singin.*
import kotlinx.android.synthetic.main.activity_signin.mobile
import kotlinx.android.synthetic.main.activity_signin.signinbtn
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class Signin_Due_Activity : AppCompatActivity(),CardHistoyRecyclerAdapter.OnItemClickListener {
    val tag = "Signin"
    val activity = this
    lateinit var utils : Utils
    lateinit var pDialog: Dialog
    lateinit var db : CardHistoryDatabase
    lateinit var adp : CardHistoyRecyclerAdapter
    var history : ArrayList<CardHistoryData?>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        setContentView(R.layout.activity_due_singin)
        utils = Utils(activity)
        pDialog = Dialog(activity)
        db = CardHistoryDatabase.getDatabase(activity)!!
        lang()


        signinbtn.setOnClickListener {
            if (mobile.text.toString().trim().isNotEmpty()){
                SendLogin(mobile.text.toString().trim())
            }else{
                mobile.setError("Enter Card No")
                //Toast.makeText(activity,"Enter Mobile No",Toast.LENGTH_LONG).show()
            }
        }

        mobile.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (::adp.isInitialized) {
                    println("submit"+ mobile.text.toString().trim())
                    adp.getFilter().filter(mobile.text.toString().trim())
                    //adapter.notifyDataSetChanged();
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    override fun onResume() {
        super.onResume()
        histoires()
    }

    fun SendLogin(mobile:String){
       /* val pDialo = ProgressDialog(activity);
        pDialo.setMessage("Loading....");
        pDialo.setIndeterminate(false);
        pDialo.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialo.setCancelable(false);
        //pDialo.setMax(3)
        pDialo.show()*/
        pDialog=Dialog(activity)
        RewardpointsAsync_search().execute()

    }

    inner class RewardpointsAsync_search : AsyncTask<String?, String?, String?>() {
        override fun onPreExecute() {
            loading_show(activity,pDialog).show()
        }

        protected override fun doInBackground(vararg strings: String?): String? {
            var result: String? = null
            val con = Connection()
            try {
                val jobj = JSONObject()
                jobj.put("username", mobile.text.toString().trim())
                jobj.put("version", BuildConfig.VERSION_CODE)
                Log.i("rewardinput", Appconstands.duelogin + "    " + jobj.toString())
                result = con.sendHttpPostjson(Appconstands.duelogin, jobj)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return result
        }

        override fun onPostExecute(resp: String?) {
//            swipeToRefresh!!.visibility = View.VISIBLE
            //  progress_lay!!.visibility = View.GONE
            // productItems1 = java.util.ArrayList()
            Log.e("resp",resp.toString())
            try {
                if (resp != null) {

                    val jobj = JSONArray(resp)
                    if (jobj.getJSONObject(0).getString("Status") == "Success") {

                        val jobject=jobj.getJSONObject(0).getJSONObject("Response")
                        val id = jobject.getString("id")
                        val name = jobject.getString("name")
                        val phone = jobject.getString("phone")
                        val cardno = jobject.getString("cardno")
                        val btn=if (jobject.has("paybtn"))jobject.getString("paybtn") else "0"

                        utils.setLogin(true)
                        utils.setUser_due(id,name, phone,cardno)
                        pDialog.dismiss()
                        val data = CardHistoryData();
                        data.id = id
                        data.username = mobile.text.toString().trim()
                        data.name = name
                        data.phone = phone
                        data.cardno = cardno
                        data.createdAt = (System.currentTimeMillis()/1000)
                        if (db.cardDao()!!.getCartExistence(mobile.text.toString().trim())==null) {

                            db!!.cardDao()!!.insertCart(data);
                        }else{
                            db!!.cardDao()!!.updateCart(data);
                        }
                        val st = Intent(this@Signin_Due_Activity,Dashboard::class.java)
                        st.putExtra("cardno",cardno)
                        st.putExtra("btn",btn)
                        startActivity(st)

                    } else {
                        toast(jobj.getJSONObject(0).getString("Response"))
                        pDialog.dismiss()

                    }
                }

            }
            catch (e:JSONException) {
                e.printStackTrace()
            }
        }
    }

    fun toast(msg:String){
        val toast=Toast.makeText(this,msg,Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER,0,0)
        toast.show()
    }

    fun histoires(){
        history = db.cardDao()!!.allHistories as ArrayList<CardHistoryData?>?
        println("history : "+history)
        if (!history.isNullOrEmpty()){
            adp = CardHistoyRecyclerAdapter(this,history!!,this)
            histories.adapter = adp
            histories.visibility=View.VISIBLE
            textView48.visibility=View.VISIBLE
        }else{
            histories.visibility=View.GONE
            textView48.visibility=View.GONE
        }
    }

    override fun OnItemClick(view: View, position: Int, viewType: Int) {
        println("clicked")
        mobile.setText(history!![position]!!.username)
        SendLogin(mobile.text.toString().trim())
    }

    fun lang(){
        textView7.setText(AppUtil.languageString("welcometo"))
        textView8.setText(AppUtil.languageString("vasanthamstores"))
        textView48.setText(AppUtil.languageString("recentlyused"))
        entercard.setText(AppUtil.languageString("entercard"))
        signinbtn.setText(AppUtil.languageString("submit"))
    }
}
