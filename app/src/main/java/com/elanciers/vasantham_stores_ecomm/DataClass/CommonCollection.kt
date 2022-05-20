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
    @SerializedName("card_no" ) var cardNo : String?             = null,
    @SerializedName("cardmsg" ) var cardmsg : String?             = null,
    @SerializedName("chit"  ) var chit  : ArrayList<Chit>  = arrayListOf(),

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

data class FundsData (

    @SerializedName("Status"   ) var Status   : String?   = null,
    @SerializedName("message"  ) var message  : String?   = null,
    @SerializedName("Response" ) var Response : FundResponse? = FundResponse()

)


data class FundResponse (

    @SerializedName("fund1" ) var fund1 : ArrayList<Fund1> = arrayListOf(),
    @SerializedName("fund2" ) var fund2 : ArrayList<Fund1> = arrayListOf()

)

data class Fund1 (

    @SerializedName("id"        ) var id       : String? = null,
    @SerializedName("fund_name" ) var fundName : String? = null

)

data class AreaData (

    @SerializedName("Status"   ) var Status   : String?             = null,
    @SerializedName("message"  ) var message  : String?             = null,
    @SerializedName("Response" ) var Response : ArrayList<AreaResponse> = arrayListOf()

)

data class AreaResponse (

    @SerializedName("id"       ) var id       : String? = null,
    @SerializedName("areaname" ) var areaname : String? = null

)

data class CheckCardData (

    @SerializedName("Status"   ) var Status   : String?   = null,
    @SerializedName("message"  ) var message  : String?   = null,
    @SerializedName("Response" ) var Response : CardCheckResponse? = CardCheckResponse()

)

data class CardCheckResponse (

    @SerializedName("card_no" ) var cardNo : String? = null

)

data class CreateCardData (

    @SerializedName("Status"   ) var Status   : String?           = null,
    @SerializedName("message"  ) var message  : String?           = null,
    @SerializedName("Response" ) var Response : ArrayList<String> = arrayListOf()

)


data class CardsData (

    @SerializedName("Status"   ) var Status   : String?             = null,
    @SerializedName("message"  ) var message  : String?             = null,
    @SerializedName("Response" ) var Response : CardsResponse = CardsResponse()

)

data class CardsResponse (

    @SerializedName("card_list"          ) var cardList         : ArrayList<CardList> = arrayListOf(),
    @SerializedName("door_delivery_list" ) var doorDeliveryList : ArrayList<DoorDeliveryList>   = arrayListOf()

)
data class CardList (

    @SerializedName("id"      ) var id      : String? = null,
    @SerializedName("name"    ) var name    : String? = null,
    @SerializedName("card_no" ) var cardNo  : String? = null,
    @SerializedName("phone"   ) var phone   : String? = null,
    @SerializedName("area"    ) var area    : String? = null,
    @SerializedName("year"    ) var year    : String? = null,
    @SerializedName("branch"  ) var branch  : String? = null,
    @SerializedName("paidamt" ) var paidamt : String? = null

)
data class DoorDeliveryList (

    @SerializedName("name"     ) var name     : String? = null,
    @SerializedName("card_no"  ) var cardNo   : String? = null,
    @SerializedName("phone"    ) var phone    : String? = null,
    @SerializedName("area"     ) var area     : String? = null,
    @SerializedName("aphone"   ) var aphone   : String? = null,
    @SerializedName("address"  ) var address  : String? = null,
    @SerializedName("landmark" ) var landmark : String? = null

)

data class BranchData (

    @SerializedName("Status"   ) var Status   : String?             = null,
    @SerializedName("Response" ) var Response : ArrayList<BranchResponse> = arrayListOf()

)

data class BranchResponse (

    @SerializedName("id"     ) var id     : String? = null,
    @SerializedName("branch" ) var branch : String? = null

)

data class CustomerData (

    @SerializedName("Status"   ) var Status   : String?   = null,
    @SerializedName("message"  ) var message  : String?   = null,
    @SerializedName("Response" ) var Response :  CustomerResponse? =  CustomerResponse()

)

data class  CustomerResponse (

    @SerializedName("id"           ) var id          : String = "",
    @SerializedName("card_year"    ) var cardYear    : String = "",
    @SerializedName("name"         ) var name        : String = "",
    @SerializedName("card_no"      ) var cardNo      : String = "",
    @SerializedName("phone"        ) var phone       : String = "",
    @SerializedName("area"         ) var area        : String = "",
    @SerializedName("loan_type"    ) var loanType    : String = "",
    @SerializedName("delivery_amt" ) var deliveryAmt : String = ""

)

data class CreateDeliveryData (

    @SerializedName("Status"   ) var Status   : String?           = null,
    @SerializedName("message"  ) var message  : String?           = null,
    @SerializedName("Response" ) var Response : ArrayList<String> = arrayListOf()

)

data class SettingsData (

    @SerializedName("Status"   ) var Status   : String?   = null,
    @SerializedName("message"  ) var message  : String?   = null,
    @SerializedName("Response" ) var Response : SettingsResponse? = SettingsResponse()

)

data class SettingsResponse (

    @SerializedName("delivery_amount" ) var deliveryAmount : String? = null,
    @SerializedName("razorpay_key"    ) var razorpayKey    : String? = null

)

data class CouponData (

    @SerializedName("Status"   ) var Status   : String?   = null,
    @SerializedName("message"  ) var message  : String?   = null,
    @SerializedName("Response" ) var Response : List<CouponResponse>? = arrayListOf()

)
data class CouponResponse (

    @SerializedName("amount"    ) var amount   : String? = null,
    @SerializedName("coupon_no" ) var couponNo : String? = null

)