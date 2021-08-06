package com.elanciers.vasantham_stores_ecomm.Database

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface CardHistoryDao {
    @get:Query("SELECT * FROM CardHistory ORDER BY createdAt Desc")
    val allHistories: List<CardHistoryData?>? //LiveData<List<CartProductData?>?>?

    @Insert
    fun insertCart(history: CardHistoryData?)

    @Update
    fun updateCart(history: CardHistoryData?)

    @Query("SELECT * FROM CardHistory WHERE id =:productCode")
    fun getProduct(productCode: String): LiveData<CardHistoryData?>?

    @Query("SELECT * FROM CardHistory WHERE username =:code")
    fun getCartExistence(code: String): CardHistoryData?

    @Delete
    fun deleteCart(history: CardHistoryData?)

    @Query("DELETE FROM CardHistory")
    fun dropCart()

    @Query("SELECT COUNT(*) FROM CardHistory")
    fun getItems() : Int?


    /*@Query("SELECT qty FROM CardHistory WHERE id =:productid")
    fun getItemQty(productid : String) : Int?

    @get:Query("SELECT * FROM CardHistory ORDER BY id")
    val allProductsWidget: List<CartHistoryData?>?*/
}