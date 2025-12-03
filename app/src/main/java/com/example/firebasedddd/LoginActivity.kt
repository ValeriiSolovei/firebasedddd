package com.example.firebasedddd

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
// The bad import that was here has been removed.
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import java.lang.Exception

class LoginActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnGoogle = findViewById<Button>(R.id.btnGoogle)
        val tvGoToSignUp = findViewById<TextView>(R.id.tvGoToSignUp)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val pass = etPassword.text.toString()
            if (email.isEmpty() || pass.isEmpty()) { Toast.makeText(this, "Enter email and password", Toast.LENGTH_SHORT).show(); return@setOnClickListener }
            AuthUtils.signInWithEmail(email, pass) { success, error ->
                if (success) goHome() else Toast.makeText(this, "login failed: $error", Toast.LENGTH_LONG).show()
            }
        }

        tvGoToSignUp.setOnClickListener { startActivity(Intent(this, SignUpActivity::class.java)) }

        // google sign in (default_web_client_id must be set in strings.xml)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(this, gso)

        btnGoogle.setOnClickListener {
            startActivityForResult(googleSignInClient.signInIntent, RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN && resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(Exception::class.java)
                if (account != null) {
                    AuthUtils.signInWithGoogle(account) { success, error ->
                        if (success) goHome() else Toast.makeText(this, "Google login failed: $error", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) { Toast.makeText(this, "Google sign in failed: ${e.localizedMessage}", Toast.LENGTH_LONG).show() }
        }
    }

    private fun goHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}
