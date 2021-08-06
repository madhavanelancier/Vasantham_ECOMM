package com.elanciers.vasantham_stores_ecomm.Database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore

import androidx.room.PrimaryKey


@Entity(tableName = "CardHistory")
class CardHistoryData {
    @PrimaryKey(autoGenerate = false)
    var id: String = ""
    @ColumnInfo(name = "username")
    var username: String? = ""
    @ColumnInfo(name = "name")
    var name: String? = ""
    @ColumnInfo(name = "phone")
    var phone: String? = ""
    @ColumnInfo(name = "cardno")
    var cardno: String? = ""
    @ColumnInfo(name = "createdAt")
    var createdAt: Long? = 0

    /*@Ignore
    var allImages: List<String>? = null
    var sale = 0f*/

    constructor(
        id: String,
        username: String?,
        name: String?,
        phone: String?,
        cardno: String?,
        createdAt: Long?
    ) {
        this.id = id
        this.username = username
        this.name = name
        this.phone = phone
        this.cardno = cardno
        this.createdAt = createdAt
    }

    @Ignore
    constructor(mHistory: CardHistoryData) {
        id = mHistory.id
        username = mHistory.username
        name = mHistory.name
        phone = mHistory.phone
        cardno = mHistory.cardno
        createdAt = mHistory.createdAt
    }

    @Ignore
    constructor() {
    }
}