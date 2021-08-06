package com.elanciers.vasantham_stores_ecomm

import android.app.Application
import com.elanciers.vasantham_stores_ecomm.Common.DBController
import com.elanciers.vasantham_stores_ecomm.Common.DatabaseManager
import com.elanciers.vasantham_stores_ecomm.Database.CardHistoryDatabase

class AppClass : Application() {
    override fun onCreate() {
        super.onCreate()
        CardHistoryDatabase.getDatabase(applicationContext)
        DatabaseManager.initializeInstance(DBController(applicationContext))
    }

}