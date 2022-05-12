package com.elanciers.vasantham_stores_ecomm.DataClass

import com.google.gson.annotations.SerializedName

data class YearsData (

    @SerializedName("Status"   ) var Status   : String?             = null,
    @SerializedName("message"  ) var message  : String?             = null,
    @SerializedName("Response" ) var Response : ArrayList<YearsResponse> = arrayListOf()

)
data class YearsResponse (

    @SerializedName("id"   ) var id   : String? = null,
    @SerializedName("from" ) var from : String? = null,
    @SerializedName("to"   ) var to   : String? = null

)

data class ChitGroupData (

    @SerializedName("Status"   ) var Status   : String?   = null,
    @SerializedName("message"  ) var message  : String?   = null,
    @SerializedName("Response" ) var Response : ChitGroupResponse? = ChitGroupResponse()

)
data class ChitGroupResponse (

    @SerializedName("msg"   ) var msg   : Int?             = null,
    @SerializedName("regno" ) var regno : Int?             = null,
    @SerializedName("chit"  ) var chit  : ArrayList<Chit>  = arrayListOf(),
    @SerializedName("group" ) var group : ArrayList<Group> = arrayListOf()

)

data class Chit (

    @SerializedName("id"         ) var id        : String? = null,
    @SerializedName("chite_name" ) var chiteName : String? = null

)

data class Group (

    @SerializedName("id"   ) var id   : String? = null,
    @SerializedName("name" ) var name : String? = null,
    @SerializedName("area" ) var area : String? = null

)