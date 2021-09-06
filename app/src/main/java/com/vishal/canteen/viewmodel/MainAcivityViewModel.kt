package com.vishal.canteen.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth


class MainAcivityViewModel : ViewModel() {
    val authh = MutableLiveData<FirebaseAuth>()
    val pincode = MutableLiveData<String>()

    init {
        pincode.value = "not set"
    }

}