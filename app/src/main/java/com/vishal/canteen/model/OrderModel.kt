package com.vishal.canteen.model

import com.google.firebase.firestore.PropertyName

class OrderModel(
    @JvmField @PropertyName(USER) var user: String? = "",
    @JvmField @PropertyName(AMOUNT) var totalAmount: Int = 0,
    @JvmField @PropertyName(ITEMS) var items: String = ""
//    var list = mutableListOf<Pair<String, Long>>()

    ) {

    companion object {
        const val USER = "user"
        const val AMOUNT = "totalAmount"
        const val ITEMS = "items"
    }
}