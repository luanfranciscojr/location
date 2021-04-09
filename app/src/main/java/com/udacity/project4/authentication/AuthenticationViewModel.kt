package com.udacity.project4.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.udacity.project4.utils.FirebaseUserLiveData

class AuthenticationViewModel  : ViewModel() {

    val authenticationState = FirebaseUserLiveData().map { user ->
        if (user != null) {
            FirebaseUserLiveData.AuthenticationState.AUTHENTICATED
        } else {
            FirebaseUserLiveData.AuthenticationState.UNAUTHENTICATED
        }
    }

}