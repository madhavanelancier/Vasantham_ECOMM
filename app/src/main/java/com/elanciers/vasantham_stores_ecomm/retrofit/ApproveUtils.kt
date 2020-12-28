package com.elanciers.vasantham_stores_ecomm.retrofit

import com.elanciers.vasantham_stores_ecomm.Common.Appconstands

object ApproveUtils {
    val BASE_URL = Appconstands.Domin

    val Get: usr
        //get() = RetrofitClient.getClient(BASE_URL).create(usr::class.java)
        get() = RetrofitCli.instance.api
}
