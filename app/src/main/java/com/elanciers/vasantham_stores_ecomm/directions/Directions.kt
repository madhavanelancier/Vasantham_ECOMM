package com.elanciers.vasantham_stores_ecomm.directions

import com.google.gson.annotations.SerializedName

data class Directions(@SerializedName("routes")
                      val routes: List<RoutesItem>,
                      @SerializedName("geocoded_waypoints")
                      val geocodedWaypoints: List<GeocodedWaypointsItem>?,
                      @SerializedName("status")
                      val status: String = "")