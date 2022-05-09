package com.elanciers.vasantham_stores_ecomm


fun main(args: Array<String>) {
    genericFunc<String>()
}

inline fun <reified T> genericFunc() {
    print(T::class)
}


