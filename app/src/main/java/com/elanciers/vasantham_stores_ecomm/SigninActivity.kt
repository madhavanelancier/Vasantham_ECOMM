package com.elanciers.vasantham_stores_ecomm

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.elanciers.vasantham_stores_ecomm.Common.Appconstands.loading_show
import com.elanciers.vasantham_stores_ecomm.Common.Utils
import com.elanciers.vasantham_stores_ecomm.retrofit.ApproveUtils
import com.elanciers.vasantham_stores_ecomm.retrofit.Resp
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_signin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SigninActivity : AppCompatActivity() {
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
        setContentView(R.layout.activity_signin)
        utils = Utils(activity)
        pDialog = Dialog(activity)

        signinbtn.setOnClickListener {
            if (mobile.text.length==10){
                SendLogin(mobile.text.toString().trim())

            }else{
                mobile.setError("Enter Mobile No")
                //Toast.makeText(activity,"Enter Mobile No",Toast.LENGTH_LONG).show()
            }
        }
    }

    fun SendLogin(mobile:String){
        val its = Intent(activity, OtpActivity::class.java)
        its.putExtra("type","signin")
        its.putExtra("otp","2525")
        its.putExtra("mobile",mobile)
        startActivity(its)
        finish()
    }


}
