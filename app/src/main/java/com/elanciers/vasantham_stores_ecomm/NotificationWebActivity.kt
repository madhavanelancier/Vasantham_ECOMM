package com.elanciers.vasantham_stores_ecomm

import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.elanciers.vasantham_stores_ecomm.Common.Appconstands
import kotlinx.android.synthetic.main.activity_notification_web.*


class NotificationWebActivity : AppCompatActivity() {
    val activity = this
    lateinit var pDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_web)
        pDialog = Dialog(activity)


        web.settings.setJavaScriptEnabled(true)
        web!!.setWebViewClient(HelloWebViewClient())
        web.getSettings().setLoadWithOverviewMode(true);
        web.getSettings().setUseWideViewPort(true);
        val settings = web.settings
        settings.cacheMode = WebSettings.LOAD_DEFAULT


        // Enable zooming in web view
        settings.setSupportZoom(false)
        settings.builtInZoomControls = false
        settings.displayZoomControls = false

        // Zoom web view text
        settings.textZoom = 100

        // Enable disable images in web view
        settings.blockNetworkImage = false
        // Whether the WebView should load image resources
        settings.loadsImagesAutomatically = true


        // More web view settings
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            settings.safeBrowsingEnabled = true  // api 26
        }
        //settings.pluginState = WebSettings.PluginState.ON
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.mediaPlaybackRequiresUserGesture = false


        // More optional settings, you can enable it by yourself
        settings.domStorageEnabled = true
        settings.setSupportMultipleWindows(true)
        settings.loadWithOverviewMode = true
        settings.allowContentAccess = true
        settings.setGeolocationEnabled(true)
        settings.allowUniversalAccessFromFileURLs = true
        settings.allowFileAccess = true

        // WebView settings
        web.fitsSystemWindows = true


        /*
            if SDK version is greater of 19 then activate hardware acceleration
            otherwise activate software acceleration
        */
        web.setLayerType(View.LAYER_TYPE_HARDWARE, null)

        web.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url.toString())
                return true
            }
        }
        web.loadUrl("https://vasanthamstore.com/notification")

    }

    private inner class HelloWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView,
            url: String
        ): Boolean {
            view.loadUrl(url)
            println("loadurl" + url)
            runOnUiThread {
                //  textView8.setText("Almost finish...")
                Appconstands.loading_show(activity, pDialog).show()

            }
            return true
        }
        override fun onPageFinished(view: WebView?, url: String?) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url)
            // pdialog!!.dismiss()
            Appconstands.loading_show(activity, pDialog).dismiss()

            //frameLayout1.visibility=View.VISIBLE
            web!!.visibility=View.VISIBLE

        }
        override fun onReceivedError(
            view: WebView?, errorCode: Int,
            description: String, failingUrl: String?
        ) {
            Log.i(
                "FragmentActivity.TAG",
                "GOT Page error : code : $errorCode Desc : $description"
            )
            //frameLayout1.visibility=View.VISIBLE
            web!!.visibility=View.VISIBLE
            //TODO We can show customized HTML page when page not found/ or server not found error.
            super.onReceivedError(view, errorCode, description, failingUrl)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (intent.hasExtra("KEY_TO_EXTRA")) {

        }
    }

}