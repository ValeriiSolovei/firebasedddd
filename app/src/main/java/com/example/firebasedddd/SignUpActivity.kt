package com.example.firebasedddd

import android.content.Intent // <-- Add this line
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

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
            if (email.isEmpty() || pass.isEmpty()) { Toast.makeText(this, "Enter email and password", Toast.LENGTH_SHORT).show(); return@setOnClickListener }
            AuthUtils.registerWithEmail(email, pass) { success, error ->
                if (success) {
                    Toast.makeText(this, "Registered", Toast.LENGTH_SHORT).show()
                    // The error was on the next line because Intent was not imported
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                } else Toast.makeText(this, "Register failed: $error", Toast.LENGTH_LONG).show()
            }
        }
    }
}
