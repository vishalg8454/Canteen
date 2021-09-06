package com.vishal.canteen.model

import com.google.firebase.firestore.PropertyName


class ItemModel(
    @JvmField @PropertyName(NAME) var itemName: String? = "",
    @JvmField @PropertyName(PRICE) var price: Int = 0,
    @JvmField @PropertyName(IMGURL) var imgURL: String = "",
    @JvmField @PropertyName(UNIT) var unit: String = ""
) {

    companion object {
        const val IMGURL = "imgURL"
        const val NAME = "itemName"
        const val PRICE = "price"
        const val UNIT = "unit"
    }
}
