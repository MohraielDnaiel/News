package com.example.newss



import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, Onboarding::class.java) // Ensure HomeActivity is imported
            startActivity(intent)
            finish() // Close MainActivity after transitioning
        }, 2000)
    }

//    override fun onStart() {
//        super.onStart()
//        val currentUser = mAuth?.currentUser
//        if(currentUser!=null)
//        {
//            val intent = Intent(this ,Home::class.java )
//            startActivity(intent)
//        }
//
//    }
}
