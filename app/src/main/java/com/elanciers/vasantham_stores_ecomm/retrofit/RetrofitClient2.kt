package com.elanciers.vasantham_stores_ecomm.retrofit

import com.elanciers.vasantham_stores_ecomm.Common.Appconstands
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient2 {
    val BASE_URL = Appconstands.Domin_due

    val Get: usr2
        //get() = RetrofitClient.getClient(BASE_URL).create(usr::class.java)
        get() = RetrofitClient.instance.api
}