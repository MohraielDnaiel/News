package com.example.newss

import android.animation.ObjectAnimator
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class Signup : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup)

        mAuth = FirebaseAuth.getInstance()

        val tvAlreadyHaveAccount = findViewById<TextView>(R.id.tv_login)
        val btnSignUp = findViewById<Button>(R.id.btn_signup)
        val etEmail = findViewById<EditText>(R.id.et_email)
        val etPassword = findViewById<EditText>(R.id.et_password)
        val etConfirmPassword = findViewById<EditText>(R.id.et_confirm_password)
        val logo = findViewById<ImageView>(R.id.img_logosignup)

        // Logo Animation
        val moveAnimation = ObjectAnimator.ofFloat(logo, View.TRANSLATION_Y, -250f, 0f)
        moveAnimation.duration = 1000
        moveAnimation.interpolator = LinearInterpolator()
        moveAnimation.start()

        tvAlreadyHaveAccount.setOnClickListener {
            startActivity(Intent(this, Signin::class.java))
        }

        btnSignUp.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                showMessage("Missing Fields", "Please enter email, password, and confirm password.")
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showMessage("Invalid Email", "Please enter a valid email format.")
                return@setOnClickListener
            }

            if (password.length < 6) {
                showMessage("Weak Password", "Password must be at least 6 characters long.")
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                showMessage("Password Mismatch", "Passwords do not match. Please try again.")
                return@setOnClickListener
            }

            // Firebase Authentication
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = mAuth.currentUser
                        user?.sendEmailVerification()
                            ?.addOnCompleteListener { verifyTask ->
                                if (verifyTask.isSuccessful) {
                                    showMessage("Sign Up Successful", "Please verify your email before logging in.") {
                                        startActivity(Intent(this, Signin::class.java))
                                        finish()
                                    }
                                } else {
                                    showMessage("Verification Failed", "Could not send verification email: ${verifyTask.exception?.message}")
                                }
                            }
                    } else {
                        showMessage("Sign Up Failed", "Authentication failed: ${task.exception?.message}")
                    }
                }
        }
    }

    // ✅ Reusable Function for Showing a Dialog
    private fun showMessage(title: String, message: String, onPositiveClick: (() -> Unit)? = null) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                onPositiveClick?.invoke()
            }
            .show()
    }
}
