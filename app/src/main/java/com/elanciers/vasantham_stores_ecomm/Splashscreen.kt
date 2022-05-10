package com.elanciers.vasantham_stores_ecomm

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.elanciers.vasantham_stores_ecomm.Common.AppUtil
import com.elanciers.vasantham_stores_ecomm.Common.Appconstands
import com.elanciers.vasantham_stores_ecomm.Common.Appconstands.Domin
import com.elanciers.vasantham_stores_ecomm.Common.Appconstands.Domin_due
import com.elanciers.vasantham_stores_ecomm.Common.Appconstands.languagefile
import com.elanciers.vasantham_stores_ecomm.Common.DownLoadFile
import com.elanciers.vasantham_stores_ecomm.Common.Utils
import com.elanciers.vasantham_stores_ecomm.retrofit.ApproveUtils.BASE_URL
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_splashscreen.*
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.nio.charset.Charset

class Splashscreen : AppCompatActivity() {
    val tag = "Welcome"
    val activity = this
    lateinit var utils: Utils
    lateinit var pDialog: Dialog
    private var dots: Array<TextView?> = emptyArray()
    private var layouts: IntArray? = null

    //private PrefManager prefManager;
    //internal var utils: Utils
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_splashscreen)
        utils = Utils(activity)
        Glide.with(this).load(R.drawable.logogif).into(imageView2);

        FirebaseMessaging.getInstance()
            .subscribeToTopic(("vasantham"))

        val currentapiVersion = Build.VERSION.SDK_INT
       /* if (currentapiVersion <= 21) {
            frameLayout1.visibility= View.GONE
            frm.visibility= View.VISIBLE
        } else {
         //   logo!!.setImageResource(R.mipmap.login_bg)
            frm.visibility= View.GONE
            frameLayout1.visibility= View.VISIBLE
            val path = "android.resource://" + packageName + "/" + R.raw.bringanim
            geoloc_anim.setVideoURI(Uri.parse(path))
            geoloc_anim.start()
            geoloc_anim.setZOrderOnTop(true)
            geoloc_anim.setOnPreparedListener(object: MediaPlayer.OnPreparedListener {
                override fun onPrepared(mp: MediaPlayer) {
                    mp.setLooping(true)
                    placeholder.setVisibility(View.GONE);
                }
            })
            //
            //imageView5!!.visibility = View.VISIBLE
        }*/

        Handler().postDelayed(Runnable {
            if (CheckingPermissionIsEnabledOrNot()) {
                /*if (utils.login()) {
                    startActivity(Intent(activity, HomeActivity::class.java))
                    finish()
                } else {
                    startActivity(Intent(activity, WelcomeScreen::class.java))
                    finish()
                }*/
                download()
            }else{
                RequestMultiplePermission()
            }
        }, 4000)

    }
    fun CheckingPermissionIsEnabledOrNot(): Boolean {

        val INTERNET = ContextCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET)
        val ACCESS_NETWORK_STATE = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_NETWORK_STATE)
        //val ACCESS_NOTIFICATION_POLICY = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_NOTIFICATION_POLICY)
        val ACCESS_FINE_LOCATION = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
        val ACCESS_COARSE_LOCATION = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
        //val CALL_PHONE = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE)

        return INTERNET == PackageManager.PERMISSION_GRANTED &&
                ACCESS_NETWORK_STATE == PackageManager.PERMISSION_GRANTED &&
                //ACCESS_NOTIFICATION_POLICY == PackageManager.PERMISSION_GRANTED &&
                ACCESS_FINE_LOCATION == PackageManager.PERMISSION_GRANTED &&
                ACCESS_COARSE_LOCATION == PackageManager.PERMISSION_GRANTED //&&
                //CALL_PHONE == PackageManager.PERMISSION_GRANTED
    }
    fun RequestMultiplePermission() {

        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(this, arrayOf<String>
        (android.Manifest.permission.INTERNET,
                android.Manifest.permission.ACCESS_NETWORK_STATE,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION//,
                //android.Manifest.permission.CALL_PHONE
        ), Appconstands.RequestPermissionCode)


    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {

            Appconstands.RequestPermissionCode ->

                if (grantResults.size > 0) {

                    val INTERNET = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val ACCESS_NETWORK_STATE = grantResults[1] == PackageManager.PERMISSION_GRANTED
                    //val ACCESS_NOTIFICATION_POLICY = grantResults[2] == PackageManager.PERMISSION_GRANTED
                    val ACCESS_FINE_LOCATION = grantResults[2] == PackageManager.PERMISSION_GRANTED
                    val ACCESS_COARSE_LOCATION = grantResults[3] == PackageManager.PERMISSION_GRANTED
                    //val CALL_PHONE = grantResults[4] == PackageManager.PERMISSION_GRANTED

                    if (INTERNET && ACCESS_NETWORK_STATE /*&& ACCESS_NOTIFICATION_POLICY*/ && ACCESS_FINE_LOCATION &&ACCESS_COARSE_LOCATION /*&&CALL_PHONE*/) {
                        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
                            /*val mLocationManager =  getSystemService(LOCATION_SERVICE) as (LocationManager);
                            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, this);*/
                           /* locationmanager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                            locationmanager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5f, activity)*/
                            if (utils.login()){
                                startActivity(Intent(activity,
                                    HomeActivity::class.java))
                                finish()
                            }else{
                                startActivity(Intent(activity,WelcomeScreen::class.java))
                                finish()
                            }
                        }
                        //Toast.makeText(this@MainFirstActivity, "Permission Granted", Toast.LENGTH_LONG).show()
                    } else {
                        //Toast.makeText(this@MainFirstActivity, "Permission Denied", Toast.LENGTH_LONG).show()
                        RequestMultiplePermission()
                    }
                }

        }
    }

    fun download() {
        val url = Domin_due + languagefile//"languages/english.js"
        val downloadRequest = DownLoadFile(1, url,
            { response ->
                val languageFile: File
                var count: Int
                try {
                    if (response != null) {
                        //Log.d(TAG," directory name " + mActivity.getCacheDir());
                        languageFile = File(this.getCacheDir(), "language.txt")
                        val output: OutputStream = FileOutputStream(languageFile)
                        output.write(response)
                        val data = ByteArray(1024)
                        val total: Long = 0
                        // flushing output
                        output.flush()
                        // closing streams
                        output.close()
                        val languageString: String = ApiCall().readFromFile(languageFile,utils.language)!!
                        println("languageString : " + AppUtil.languageString("app_name"))
                        if (utils.login()) {
                            startActivity(Intent(this, HomeActivity::class.java))
                            finish()
                        } else {
                            startActivity(Intent(this, WelcomeScreen::class.java))
                            finish()
                        }
                    }
                    //volleyCallback.setDataResponse(null)
                    //Utils.cancelProgressBar()
                } catch (e: Exception) {
                    //Utils.cancelProgressBar()
                    //Log.d("KEY_ERROR","UNABLE TO DOWNLOAD FILE");
                    e.printStackTrace()
                    //Utils.showCyouAlert(mActivity,mActivity.getString(R.string.app_name),Utils.languageString("networkerror"));
                }
            }, { error ->
                //Utils.cancelProgressBar()
                var errorCode: Int
                val errorMessage = ""
                try {
                    val responseBody = String(error.networkResponse.data, Charset.forName("utf-8"))
                    val data = JSONObject(responseBody)
                    println("error data : " + data)
                    if (data.has("msg")) {
                        /*Utils.showCyouAlert(
                            mActivity,
                            mActivity.getString(R.string.app_name),
                            Utils.languageString(data.getString("msg"))
                        )*/
                    } else if (data.has("message")) {
                        /*Utils.showCyouAlert(
                            mActivity,
                            mActivity.getString(R.string.app_name),
                            Utils.languageString(data.getString("message"))
                        )*/
                    } else {
                        //Utils.showCyouAlert(mActivity,mActivity.getString(R.string.app_name),Utils.languageString("networkerror"));
                    }
                } catch (e: Exception) {
                    println("exception : " + e.toString())
                    e.printStackTrace()
                    //Utils.showCyouAlert(mActivity,mActivity.getString(R.string.app_name),Utils.languageString("networkerror"));
                }
                val alert = AlertDialog.Builder(this)
                alert.setCancelable(false)
                alert.setTitle(AppUtil.languageString("connection_error_title"))
                alert.setMessage(AppUtil.languageString("connection_error"))
                alert.setPositiveButton(
                    AppUtil.languageString("connection_error_button"),
                    object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            if (Appconstands.net_status(this@Splashscreen)) {
                                dialog!!.dismiss()
                                download()
                            }
                        }
                    })
                alert.show()
            }, null
        )
        val mRequestQueue = Volley.newRequestQueue(this)
        mRequestQueue.add<ByteArray>(downloadRequest)
    }
}