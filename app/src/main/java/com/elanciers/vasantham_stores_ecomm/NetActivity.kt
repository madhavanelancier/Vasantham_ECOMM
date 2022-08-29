package com.elanciers.vasantham_stores_ecomm

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_net.*

class NetActivity : AppCompatActivity(),NetworkStateReceiver.NetworkStateReceiverListener {
    private var networkStateReceiver: NetworkStateReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_net)

        button2.setOnClickListener {
                finish()

        }


    }
    fun startNetworkBroadcastReceiver(currentContext: Context?) {
        networkStateReceiver = NetworkStateReceiver()
        networkStateReceiver!!.addListener(currentContext as NetworkStateReceiver.NetworkStateReceiverListener?)
        registerNetworkBroadcastReceiver(currentContext!!)
    }
    fun unregisterNetworkBroadcastReceiver(currentContext: Context) {
        currentContext.unregisterReceiver(networkStateReceiver)
    }
    fun registerNetworkBroadcastReceiver(currentContext: Context) {
        currentContext.registerReceiver(
            networkStateReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    override fun networkUnavailable() {

    }

    override fun networkAvailable() {
        Log.i("TAG", "networkAvailable()")
        //interblk.visibility=View.GONE
        finish()
        //Proceed with online actions in activity (e.g. hide offline UI from user, start services, etc...)
    }
}