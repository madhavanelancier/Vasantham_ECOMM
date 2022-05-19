package com.elanciers.vasantham_stores_ecomm


fun main(args: Array<String>) {
    genericFunc<String>()
}

inline fun <reified T> genericFunc() {
    print(T::class)
}

/*
"creat_card": "Create Card",
"select_year": "Select Year",
"selecte_chit_group": "Select Chit Group",
"number": "Number",
"card_number": "Card Number",
"mobile_number": "Mobile Number",
"address": "Address",
"select_group": "Select Group",
"select_area": "Select Area",
"agent": "Agent",
"select_fund1": "Select Fund1",
"selec_fund2": "Select Fund2",
"cards": "Cards",
"date": "Date",
"custmer_name": "Customer Name",
"customer_phone": "Customer Phone",
"delivery_amount": "Delivery Amount",
"address_line1": "Address Line 1",
"address_line2": "Address Line 2",
"landmark": "Landmark",
"pincode": "Pincode",
"door_delivery": "Door Delivery",
"alt_mobile_number": "Alternate Mobile Number",
*/
