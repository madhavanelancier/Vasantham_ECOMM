package com.elanciers.vasantham_stores_ecomm.Common

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.preference.PreferenceManager
import com.elanciers.vasantham_stores_ecomm.DataClass.SettingsResponse

class Utils(internal var _context: Context) {
    internal var sharedPreferences: SharedPreferences

    val isConnectingToInternet: Boolean
        get() {
            val connectivity =
                _context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivity != null) {
                val info = connectivity.allNetworkInfo
                if (info != null)
                    for (i in info.indices)
                        if (info[i].state == NetworkInfo.State.CONNECTED) {
                            return true
                        }

            }
            return false
        }

    init {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context)
    }

    fun savePreferences(string: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(string, value)
        editor.commit()
    }

    fun clear() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.commit()
    }

    fun login():Boolean{
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getBoolean("login", false)
    }
    fun setLogin(t : Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("login", t)
        editor.commit()
    }
    fun setMpin(t : String) {
        val editor = sharedPreferences.edit()
        editor.putString("mpin", t)
        editor.commit()
    }

    fun userid():String{
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("id", "")!!
    }
    fun mpin():String{
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("mpin", "")!!
    }
    fun name():String?{
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("name", "")
    }
    fun mobile():String?{
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("mobile", "")
    }
    fun email():String?{
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("email", "")
    }
    fun dob():String?{
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("dob", "")
    }
    fun gender():String?{
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("gender", "")
    }
    fun name_due():String?{
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("due_name", "")
    }
    fun mobile_due():String?{
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("due_mobile", "")
    }
    fun card_due():String?{
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("due_card", "")
    }
    fun due_id():String{
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("dueid", "")!!
    }

    fun setUser(id:String,name:String,mobile:String,email:String,dob:String,gender:String) {
        val editor = sharedPreferences.edit()
        editor.putString("id", id)
        editor.putString("name", name)
        editor.putString("mobile", mobile)
        editor.putString("email", email)
        editor.putString("dob", dob)
        editor.putString("gender", gender)
        editor.commit()
    }

    fun setUser_due(id:String,name:String,mobile:String,email:String) {
        val editor = sharedPreferences.edit()
        editor.putString("dueid", id)
        editor.putString("due_name", name)
        editor.putString("due_mobile", mobile)
        editor.putString("due_card", email)
        editor.commit()
    }

    fun setVendor(typeid:String,vendor_id:String,vendor_nm:String,vendor_img:String) {
        val editor = sharedPreferences.edit()
        editor.putString("typeid", typeid)
        editor.putString("vendor_id", vendor_id)
        editor.putString("vendor_nm", vendor_nm)
        editor.putString("vendor_img", vendor_img)
        editor.commit()
    }

    fun setToken(t : String) {
        val editor = sharedPreferences.edit()
        editor.putString("token", t)
        editor.commit()
    }

    fun getToken():String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("token", "")
    }

    fun gettypeid():String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("typeid", "")
    }

    fun getvendor_id():String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("vendor_id", "")
    }

    fun getvendor_nm():String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("vendor_nm", "")
    }

    fun getvendor_img():String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("vendor_img", "")
    }

    fun getValue(str: String): String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString(str, "")
    }

    fun setMaintenance(t : String) {
        val editor = sharedPreferences.edit()
        editor.putString("maintenance", t)
        editor.commit()
    }
    fun Maintenance():String?{
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("maintenance", "")
    }

    fun setServiceState(b: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("service", b)
        editor.commit()
    }

    fun getServiceState(): Boolean {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getBoolean("service", false)
    }

    fun setSettings(settings: SettingsResponse?) {
        if (settings!=null) {
            val editor = sharedPreferences.edit()
            editor.putString("razorpay_key", settings.razorpayKey)
            editor.putString("delivery_amount", settings.deliveryAmount)
            editor.putString("online_card", settings.online_card)
            editor.putString("online_door", settings.online_door)
            editor.commit()
        }
    }

    val razorpay_key get() =sharedPreferences.getString("razorpay_key", Appconstands.razorpay_key)!!
    val language get() =sharedPreferences.getString("language", "English")!!
}
