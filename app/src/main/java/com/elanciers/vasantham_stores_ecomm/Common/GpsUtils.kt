package com.elanciers.vasantham_stores_ecomm.Common

import android.app.Activity
import android.content.Context
import android.content.IntentSender.SendIntentException
import android.location.LocationManager
import android.util.Log
import android.widget.Toast
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.plus.PlusOneDummyView
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener

class GpsUtils(private val context: Activity) {
    private val mSettingsClient: SettingsClient
    private val mLocationSettingsRequest: LocationSettingsRequest
    private val locationManager: LocationManager
    private val locationRequest: LocationRequest
    // method for turn on GPS
    fun turnGPSOn(onGpsListener: onGpsListener?) {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            onGpsListener?.gpsStatus(true)
        } else {
            mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(
                    context,
                    OnSuccessListener<LocationSettingsResponse?> {
                        //  GPS is already enable, callback GPS status through listener
                        onGpsListener?.gpsStatus(true)
                    })
                .addOnFailureListener(context, OnFailureListener { e ->
                    val statusCode = (e as ApiException).statusCode
                    when (statusCode) {
                        LocationSettingsStatusCodes.SUCCESS -> {

                        }//try { // Show the dialog by calling startResolutionForResult(), and check the
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try { // Show the dialog by calling startResolutionForResult(), and check the
// result in onActivityResult().
                            val rae = e as ResolvableApiException
                            rae.startResolutionForResult(context, Appconstands.GPS_REQUEST)
                        } catch (sie: SendIntentException) {
                            Log.i(
                                PlusOneDummyView.TAG,
                                "PendingIntent unable to execute request."
                            )
                        }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                            val errorMessage =
                                "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings."
                            Log.e(PlusOneDummyView.TAG, errorMessage)
                            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                        }
                    }
                })
        }
    }

    interface onGpsListener {
        fun gpsStatus(isGPSEnable: Boolean)
    }

    init {
        locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        mSettingsClient = LocationServices.getSettingsClient(context)
        locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10 * 1000.toLong()
        locationRequest.fastestInterval = 2 * 1000.toLong()
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        mLocationSettingsRequest = builder.build()
        //**************************
        builder.setAlwaysShow(true) //this is the key ingredient
        //**************************
    }
}