package com.elanciers.vasantham_stores_ecomm

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.elanciers.vasantham_stores_ecomm.Common.Appconstands
import com.elanciers.vasantham_stores_ecomm.Common.Appconstands.loading_show
import com.elanciers.vasantham_stores_ecomm.Common.DBController
import com.elanciers.vasantham_stores_ecomm.Common.Utils
import com.elanciers.vasantham_stores_ecomm.DataClass.AddressData
import com.elanciers.vasantham_stores_ecomm.retrofit.ApproveUtils
import com.elanciers.vasantham_stores_ecomm.retrofit.Resp
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import kotlinx.android.synthetic.main.activity_otp.*
import kotlinx.android.synthetic.main.coupon_adapter.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.concurrent.TimeUnit

class OtpActivity : AppCompatActivity() {
    val tag = "Otp"
    val activity = this
    lateinit var utils : Utils
    lateinit var db : DBController
    lateinit var pDialog: Dialog
    private var mOTPStr = ""
    var codes = ""
    var type = ""
    var otp = ""
    var otpnew = ""
    var mVerificationId = ""
    var firebaseAuth: FirebaseAuth? = null
    var changedCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks? = null
    var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance()
        mAuth = FirebaseAuth.getInstance();
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        setContentView(R.layout.activity_otp)
        utils = Utils(activity)
        pDialog = Dialog(activity)
        db = DBController(activity)

        type = intent!!.extras!!.getString("type").toString()
        otp = intent!!.extras!!.getString("otp").toString()

        if(intent.extras!!.getString("mobile").toString()!="8144688721"){
            //if (!BuildConfig.DEBUG) {
                sendVerificationCode(intent.extras!!.getString("mobile").toString());
            //}
        }
        //else{

        //}

        back.setOnClickListener {
            finish()
        }
        println("otp : " + otp)



        submit.setOnClickListener {
            if (enotp.text.toString().trim().isNotEmpty()) {
                if (type == "signup") {
                    verifyVerificationCode(enotp.text.toString().trim())
                } else {
                    verifyVerificationCode(enotp.text.toString().trim())
                }
            }
            else{

                enotp.setError("Enter Otp")
            }
        }
    }

    fun toast(msg:String){
        val toast=Toast.makeText(applicationContext,msg,Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER,0,0)
        toast.show()
    }

    private fun sendVerificationCode(mobile:String) {
        pDialog=Dialog(activity)
        Log.e("mob",mobile)
        loading_show(activity,pDialog).show()
        /*PhoneAuthProvider.getInstance(firebaseAuth!!).verifyPhoneNumber(
            "+91$mobile",
            60,
            TimeUnit.SECONDS,
            activity,
            mCallbacks
        )*/
        val call = ApproveUtils.Get.SendOTP(mobile)
        call.enqueue(object : Callback<Resp> {
            override fun onResponse(call: Call<Resp>, response: Response<Resp>) {
                Log.e("$tag response", response.toString())
                if (response.isSuccessful()) {
                    val example = response.body() as Resp
                    println(example)
                    Toast.makeText(activity, example.message, Toast.LENGTH_LONG).show()
                }
                pDialog.dismiss()
                //loading_show(activity).dismiss()
            }

            override fun onFailure(call: Call<Resp>, t: Throwable) {
                Log.e("$tag Fail response", t.toString())
                if (t.toString().contains("time")) {
                    Toast.makeText(
                        activity,
                        "Poor network connection",
                        Toast.LENGTH_LONG
                    ).show()
                }
                pDialog.dismiss()
                //loading_show(activity).dismiss()
            }
        })
    }

    private val mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                //Getting the code sent by SMS
                val code = phoneAuthCredential.smsCode

                //sometime the code is not detected automatically
                //in this case the code will be null
                //so user has to manually enter the code
                if (code != null) {
                    otpnew = code
                    println("otp : " + code)
                    enotp.setText(code)
                    //verifying the code
                    verifyVerificationCode(code)
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.d(activity.javaClass.name,e.message!!)
                Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
                if (pDialog.isShowing){
                    pDialog.dismiss()
                }
            }

            override fun onCodeSent(s: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(s, forceResendingToken)
                mVerificationId = s

                //mResendToken = forceReforceResendingTokensendingToken
                //mobile.isEnabled = false
                //submit.setText("Login To continue")
                //enotp.visibility = View.VISIBLE
                Toast.makeText(
                    activity,
                    "OTP has been sent",
                    Toast.LENGTH_LONG
                ).show()

                if (pDialog.isShowing){
                    pDialog.dismiss()
                }
            }
        }

    private fun verifyVerificationCode(otp: String) {
        //creating the credential
        //val credential = PhoneAuthProvider.getCredential(mVerificationId, otp)

        //signing the user
        //signInWithPhoneAuthCredential(credential)
        if (enotp.text.toString().trim().isNotEmpty()) {
            ///if(otpnew==enotp.text.toString().trim()) {

            if (type == "signup") {
                val name = intent.extras!!.getString("name")
                val mobile = intent.extras!!.getString("mobile")
                val email = intent.extras!!.getString("email")
                CheckSignupOtp(
                    name.toString(),
                    mobile.toString(),
                    email.toString(),
                    enotp.text.toString().trim(),
                    enotp.text.toString().trim()
                )
            } else {
                val mobile = intent.extras!!.getString("mobile")
                CheckSigninOtp(mobile.toString(), enotp.text.toString().trim(), enotp.text.toString().trim())
            }
            // }
            /*else{
                toast("Invalid OTP")
            }*/

        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        pDialog=Dialog(activity)
        loading_show(activity, pDialog).show()
        firebaseAuth!!.signInWithCredential(credential).addOnCompleteListener(this,
            OnCompleteListener { task: Task<AuthResult?> ->
                try {
                    if (task.isSuccessful) {
                        //updateStatusLogin("success", mobileNO)
                        if (enotp.text.toString().trim().isNotEmpty()) {
                            ///if(otpnew==enotp.text.toString().trim()) {

                                if (type == "signup") {
                                    val name = intent.extras!!.getString("name")
                                    val mobile = intent.extras!!.getString("mobile")
                                    val email = intent.extras!!.getString("email")
                                    CheckSignupOtp(
                                        name.toString(),
                                        mobile.toString(),
                                        email.toString(),
                                        enotp.text.toString().trim(),
                                        enotp.text.toString().trim()
                                    )
                                } else {
                                    val mobile = intent.extras!!.getString("mobile")
                                    CheckSigninOtp(mobile.toString(), enotp.text.toString().trim(), enotp.text.toString().trim())
                                }
                           // }
                            /*else{
                                toast("Invalid OTP")
                            }*/

                        }
                    } else {

                        toast("Please enter OTP")
                        //updateStatusLogin("failed", mobileNO)
                    }
                } catch (e: Exception) {
                    FirebaseAuth.getInstance().signOut()
                    e.printStackTrace()
                }
            })
    }


    fun CheckSigninOtp(mob: String, otp: String, eotp: String){

        pDialog=Dialog(activity)
        loading_show(activity, pDialog).show()
        println("mobile"+mob)
        println("res_otp"+otp)
        println("enter_otp"+otp)
        println("type"+"")
        val call = ApproveUtils.Get.Otp("1", mob, otp)
        call.enqueue(object : Callback<Resp> {
            override fun onResponse(call:Call<Resp>, response:Response<Resp>) {
                Log.e("$tag responce", response.toString())
                if (response.isSuccessful()) {
                    val example = response.body() as Resp
                    println(example)
                    if (example.status == "Success") {
                        val ad = response.body() as Resp
                        val userid =
                            if (ad.response!![0].id.isNullOrEmpty()) "" else ad.response!![0].id.toString()
                        val name =
                            if (ad.response!![0].name.isNullOrEmpty()) "" else ad.response!![0].name.toString()
                        val email =
                            if (ad.response!![0].email.isNullOrEmpty()) "" else ad.response!![0].email.toString()
                        val mobile = if (ad.response!![0].mobile.isNullOrEmpty()) "" else ad.response!![0].mobile.toString()
                        val dob = if (ad.response!![0].dob.isNullOrEmpty()) "" else ad.response!![0].dob.toString()
                        val gender = if (ad.response!![0].gender.isNullOrEmpty()) "" else ad.response!![0].gender.toString()
                        val address = ad.response!![0].address
                        utils.setUser(userid, name, mobile, email,dob, gender)
                        println("userid : " + userid)
                        utils.setLogin(true)
                       /* for (ads in 0 until address!!.size) {
                            val adrs_id =
                                if (address[ads].id.isNullOrEmpty()) "" else address[ads].id.toString()
                            val type =
                                if (address[ads].type.isNullOrEmpty()) "" else address[ads].type.toString()
                            val title =
                                if (address[ads].title.isNullOrEmpty()) "" else address[ads].title.toString()
                            val flat_no =
                                if (address[ads].flat_no.isNullOrEmpty()) "" else address[ads].flat_no.toString()
                            val area =
                                if (address[ads].area.isNullOrEmpty()) "" else address[ads].area.toString()
                            val location =
                                if (address[ads].location.isNullOrEmpty()) "" else address[ads].location.toString()
                            val lat =
                                if (address[ads].lat.isNullOrEmpty()) "" else address[ads].lat.toString()
                            val lng =
                                if (address[ads].lng.isNullOrEmpty()) "" else address[ads].lng.toString()
                            val rs = AddressData()
                            rs.adrs_id = adrs_id
                            rs.adrs_type = type
                            rs.adrs_title = title
                            rs.address = location
                            rs.adrs_house = flat_no
                            rs.adrs_landmark = area
                            rs.adrs_latitude = lat
                            rs.adrs_longtitude = lng
                            db.AddressInsert(rs)
                        }*/
                        Toast.makeText(
                            activity,
                            example.message,
                            Toast.LENGTH_LONG
                        ).show()
                        startActivity(Intent(activity, HomeActivity::class.java))
                        finish()
                    } else {
                        pDialog.dismiss()
                        Toast.makeText(
                            activity,
                            example.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                pDialog.dismiss()
                //loading_show(activity).dismiss()
            }

            override fun onFailure(call: Call<Resp>, t: Throwable) {
                Log.e("$tag Fail response", t.toString())
                if (t.toString().contains("time")) {
                    Toast.makeText(
                        activity,
                        "Poor network connection",
                        Toast.LENGTH_LONG
                    ).show()
                }
                pDialog.dismiss()
                //loading_show(activity).dismiss()
            }
        })
    }

    fun CheckSignupOtp(name: String, mob: String, email: String, otp: String, eotp: String){

        println("mobile_Signup"+mob)
        println("res_otp_Signup"+otp)
        println("enter_otp_Signup"+otp)
        println("type_Signup"+"")
       // Appconstands.loading_show(activity, pDialog).show()

        val call = ApproveUtils.Get.Otp_Reg("1", mob, otp,name,email)
        call.enqueue(object : Callback<Resp> {
            override fun onResponse(call: Call<Resp>, response: Response<Resp>) {
                Log.e("$tag responce", response.toString())
                if (response.isSuccessful()) {
                    val example = response.body() as Resp
                    println("success"+example)
                    if (example.status == "Success") {
                        val ad = response.body() as Resp
                        val userid =
                            if (ad.message.isNullOrEmpty()) "" else ad.message.toString()
                        /*val name = if (ad.response!![0].name.isNullOrEmpty())"" else ad.response!![0].name.toString()
                        val email = if (ad.response!![0].email.isNullOrEmpty())"" else ad.response!![0].email.toString()
                        val mobile = if (ad.response!![0].mobile.isNullOrEmpty())"" else ad.response!![0].mobile.toString()
                        val address = ad.response!![0].address*/
                        utils.setUser(userid, name, mob, email,"","")
                        utils.setLogin(true)
                        /*for (ads in 0 until address!!.size){
                            val type = if (address[ads].type.isNullOrEmpty())"" else address[ads].type.toString()
                            val title = if (address[ads].title.isNullOrEmpty())"" else address[ads].title.toString()
                            val flat_no = if (address[ads].flat_no.isNullOrEmpty())"" else address[ads].flat_no.toString()
                            val area = if (address[ads].area.isNullOrEmpty())"" else address[ads].area.toString()
                            val location = if (address[ads].location.isNullOrEmpty())"" else address[ads].location.toString()
                            val lat = if (address[ads].lat.isNullOrEmpty())"" else address[ads].lat.toString()
                            val lng = if (address[ads].lng.isNullOrEmpty())"" else address[ads].lng.toString()
                            val rs = AddressData()
                            rs.adrs_id=ads.toString()
                            rs.adrs_type=type
                            rs.adrs_title=title
                            rs.address=location
                            rs.adrs_house=flat_no
                            rs.adrs_landmark=area
                            rs.adrs_latitude=lat
                            rs.adrs_longtitude=lng
                            db.AddressInsert(rs)
                        }*/
                       /* Toast.makeText(
                            activity,
                            example.message,
                            Toast.LENGTH_LONG
                        ).show()*/
                        startActivity(Intent(activity, HomeActivity::class.java))
                        finish()
                    } else {
                        pDialog.dismiss()
                        Toast.makeText(
                            activity,
                            example.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                pDialog.dismiss()

            }

            override fun onFailure(call: Call<Resp>, t: Throwable) {
                Log.e("$tag Fail response", t.toString())
                if (t.toString().contains("time")) {
                    Toast.makeText(
                        activity,
                        "Poor network connection",
                        Toast.LENGTH_LONG
                    ).show()
                }
                pDialog.dismiss()
                //Appconstands.loading_show(activity).dismiss()
            }
        })
    }
}
