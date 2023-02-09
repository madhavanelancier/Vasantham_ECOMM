package com.elanciers.vasantham_stores_ecomm.retrofit

import com.elanciers.vasantham_stores_ecomm.DataClass.PovaData
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Resp {

    @SerializedName("Status")
    @Expose
    var status: String? = null
    @SerializedName("Message")
    @Expose
    var message: String? = null
    @SerializedName("Response")
    @Expose
    var response: List<PovaData>? = null

    inner  class Resp2 {

        @SerializedName("id")
        @Expose
        var id: Int? = null
        @SerializedName("version")
        @Expose
        var version: Int? = null

    }
    inner  class FailResp {

        @SerializedName("Status")
        @Expose
        var status: String? = null
        @SerializedName("Response")
        @Expose
        var response: String? = null

    }
}
