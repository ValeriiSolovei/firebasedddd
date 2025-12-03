package com.example.firebasedddd

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnSignUp = findViewById<Button>(R.id.btnSignUp)

        btnSignUp.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val pass = etPassword.text.toString()
            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            AuthUtils.registerWithEmail(email, pass) { success, error ->
                if (success) {
                    val userId = AuthUtils.getCurrentUserId()
                    if (userId != null) {
                        // Create a user map to store in Firestore
                        val user = hashMapOf(
                            "uid" to userId,
                            "email" to email
                            // Add other fields like name, userType, etc. if needed
                        )

                        // Get Firestore instance and save the user
                        FirebaseFirestore.getInstance().collection("users").document(userId)
                            .set(user)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Registered", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, HomeActivity::class.java))
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Failed to save user data: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                    } else {
                        Toast.makeText(this, "Registration failed: Could not get user ID", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this, "Register failed: $error", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
