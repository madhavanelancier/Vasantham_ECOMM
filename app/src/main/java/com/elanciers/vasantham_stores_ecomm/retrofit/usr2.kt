package com.elanciers.vasantham_stores_ecomm.retrofit

import com.elanciers.vasantham_stores_ecomm.DataClass.*
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface usr2 {

    @POST("collection_common.php")
    fun getYears(@Body()obj : JsonObject): Call<YearsData>

    @POST("collection_common.php")
    fun getChitGroup(@Body()obj : JsonObject): Call<ChitGroupData>

    @POST("collection_common.php")
    fun getFunds(@Body()obj : JsonObject): Call<FundsData>

    @POST("collection_common.php")
    fun getproplace(@Body()obj : JsonObject): Call<ChitGroupData>

    @POST("collection_common.php")
    fun getAreas(@Body()obj : JsonObject): Call<AreaData>

    @POST("collection_common.php")
    fun CheckCard(@Body()obj : JsonObject): Call<CheckCardData>

    /*@POST("card_create.php")
    fun CreatCard(@Body()obj : JsonObject): Call<CreateCardData>*/

    @POST("online_customer.php")
    fun CreatCard(@Body()obj : JsonObject): Call<CreateCardData>

    @POST("collection_common.php")
    fun getCards(@Body()obj : JsonObject): Call<CardsData>

    @GET("branch_list.php")
    fun getBranchs(): Call<BranchData>

    @POST("collection_common.php")
    fun getCustomer(@Body()obj : JsonObject): Call<CustomerData>

    @POST("door_delivery.php")
    fun CreateDelivery(@Body()obj : JsonObject): Call<CreateDeliveryData>

    @GET("settings.php")
    fun getSettings(): Call<SettingsData>

    @POST("coupon.php")
    fun CheckCopon(@Body()obj : JsonObject): Call<CouponData>

    @POST()
    fun getLoyaltypoints(@Url() url: String,@Body()obj : JsonObject): Call<LoyaltyPoints>

    @POST()
    fun getLoyaltypointsList(@Url() url: String,@Body()obj : JsonObject): Call<LoyaltyList>


}