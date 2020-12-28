package com.elanciers.vasantham_stores_ecomm.nearbySearch

import com.google.gson.annotations.SerializedName

data class PhotosItem(@SerializedName("photo_reference")
                      val photoReference: String = "",
                      @SerializedName("width")
                      val width: Int = 0,
                      @SerializedName("html_attributions")
                      val htmlAttributions: List<String>?,
                      @SerializedName("height")
                      val height: Int = 0)