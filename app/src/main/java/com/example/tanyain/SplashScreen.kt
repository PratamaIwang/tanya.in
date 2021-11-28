package com.example.tanyain

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay

class SplashScreen : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        mAuth = Firebase.auth

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({

            if(mAuth.currentUser==null){
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this,DashActivity::class.java))
                finish()
            }

        },3000)
    }
}