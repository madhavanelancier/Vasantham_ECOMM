package com.elanciers.vasantham_stores_ecomm

import android.app.Dialog
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.elanciers.vasantham_stores_ecomm.Common.Appconstands
import com.elanciers.vasantham_stores_ecomm.Common.Appconstands.loading_show
import com.elanciers.vasantham_stores_ecomm.Common.Connection
import com.elanciers.vasantham_stores_ecomm.Common.Utils
import kotlinx.android.synthetic.main.activity_signin.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class Signin_Due_Activity : AppCompatActivity() {
    val tag = "Signin"
    val activity = this
    lateinit var utils : Utils
    lateinit var pDialog: Dialog
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

        signinbtn.setOnClickListener {
            if (mobile.text.toString().trim().isNotEmpty()){
                SendLogin(mobile.text.toString().trim())
            }else{
                mobile.setError("Enter Card No")
                //Toast.makeText(activity,"Enter Mobile No",Toast.LENGTH_LONG).show()
            }
        }
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
                Log.i("rewardinput", Appconstands.duelogin + "    " + jobj.toString())
                result = con.sendHttpPostjson2(Appconstands.duelogin, jobj, "")
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return result
        }

        override fun onPostExecute(resp: String?) {
//            swipeToRefresh!!.visibility = View.VISIBLE
            //  progress_lay!!.visibility = View.GONE
            // productItems1 = java.util.ArrayList()
            try {
                if (resp != null) {

                    val jobj = JSONArray(resp)
                    if (jobj.getJSONObject(0).getString("Status") == "Success") {

                        val jobject=jobj.getJSONObject(0).getJSONObject("Response")

                        utils.setLogin(true)
                        utils.setUser_due(jobject.getString("id"),jobject.getString("name"),
                                      jobject.getString("phone"),jobject.getString("cardno"))
                        pDialog.dismiss()
                        startActivity(Intent(this@Signin_Due_Activity,Dashboard::class.java))

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
        val toast=Toast.makeText(this,msg,Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER,0,0)
        toast.show()
    }
}
