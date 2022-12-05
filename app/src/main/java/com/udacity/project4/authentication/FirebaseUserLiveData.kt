package com.udacity.project4.authentication

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirebaseUserLiveData : LiveData<FirebaseUser?>() {

    private val _myFireAuth = FirebaseAuth.getInstance()

    private val authStateListener = FirebaseAuth.AuthStateListener { auth ->
        value = auth.currentUser
    }

    override fun onInactive() {
        _myFireAuth.removeAuthStateListener(authStateListener)
    }
    override fun onActive() {
        _myFireAuth.addAuthStateListener(authStateListener)
    }
}