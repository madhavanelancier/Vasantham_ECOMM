package com.elanciers.vasantham_stores_ecomm.retrofit

import com.elanciers.vasantham_stores_ecomm.directions.Directions
import com.elanciers.vasantham_stores_ecomm.nearbySearch.NearbySearch
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleMethods {
    // Google Place API -- Nearby search
    @GET("place/nearbysearch/json")
    fun getNearbySearch(
        @Query("location") location: String,
        @Query("radius") radius: String,
        @Query("type") types: String,
        @Query("key") key: String
    ): Call<NearbySearch>

    // Google Directions API -- directions
    @GET("directions/json")
    fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") key: String
    ): Call<Directions>
}