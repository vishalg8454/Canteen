package com.vishal.canteen.model

import com.google.firebase.firestore.PropertyName

class CategoryModel(
    @JvmField @PropertyName(CATEGORYNAME) var categoryName: String? = "",
    @JvmField @PropertyName(IMGURL) var imgURL: String? = ""
) {

    companion object {
        const val CATEGORYNAME = "categoryName"
        const val IMGURL = "imgURL"
    }
}
