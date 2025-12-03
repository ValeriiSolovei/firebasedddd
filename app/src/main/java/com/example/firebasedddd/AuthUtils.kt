package com.example.firebasedddd

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

object AuthUtils {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun registerWithEmail(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { t ->
                if (t.isSuccessful) onComplete(true, null) else onComplete(false, t.exception?.localizedMessage)
            }
    }

    fun signInWithEmail(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { t ->
                if (t.isSuccessful) onComplete(true, null) else onComplete(false, t.exception?.localizedMessage)
            }
    }

    fun signInWithGoogle(account: GoogleSignInAccount, onComplete: (Boolean, String?) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { t ->
                if (t.isSuccessful) onComplete(true, null) else onComplete(false, t.exception?.localizedMessage)
            }
    }

    fun signOut() { auth.signOut() }

    fun getCurrentUserId(): String? = auth.currentUser?.uid
}
