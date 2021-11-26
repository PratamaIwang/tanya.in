package com.example.tanyain

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth

class DashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(FirebaseAuth.getInstance().currentUser==null){
            startActivity(Intent(this, LoginActivity::class.java))
        }else{
            setContentView(R.layout.activity_dash)
        }
    }
}