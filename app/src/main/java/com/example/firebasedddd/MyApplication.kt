package com.example.firebasedddd

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Firebase first
        FirebaseApp.initializeApp(this)

        // Get an instance of FirebaseAppCheck
        val firebaseAppCheck = FirebaseAppCheck.getInstance()

        // Install the debug provider factory
        firebaseAppCheck.installAppCheckProviderFactory(
            DebugAppCheckProviderFactory.getInstance()
        )
    }
}
