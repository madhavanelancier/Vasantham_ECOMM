package com.elanciers.vasantham_stores_ecomm

import android.app.Application
import com.elanciers.vasantham_stores_ecomm.Common.DBController
import com.elanciers.vasantham_stores_ecomm.Common.DatabaseManager

class AppClass : Application() {
    override fun onCreate() {
        super.onCreate()
        DatabaseManager.initializeInstance(DBController(applicationContext))
    }

}