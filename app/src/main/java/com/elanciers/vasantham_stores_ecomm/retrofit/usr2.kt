package com.elanciers.vasantham_stores_ecomm.retrofit

import com.elanciers.vasantham_stores_ecomm.DataClass.ChitGroupResponse
import com.elanciers.vasantham_stores_ecomm.DataClass.YearsData
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface usr2 {

    @POST("collection_common.php")
    fun getYears(@Body()obj : JsonObject): Call<YearsData>

    @POST("collection_common.php")
    fun getChitGroup(@Body()obj : JsonObject): Call<ChitGroupResponse>
}