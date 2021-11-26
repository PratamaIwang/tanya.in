package com.example.tanyain

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var inpEmail: EditText
    private lateinit var inpPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var createAccount: TextView

    private lateinit var mAuth: FirebaseAuth

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        inpEmail = findViewById(R.id.inpEmail)
        inpPassword = findViewById(R.id.inpPassword)
        btnLogin = findViewById(R.id.btn_login)
        createAccount = findViewById(R.id.tv_createAccount)

        mAuth = Firebase.auth

        btnLogin.setOnClickListener{
            var email = inpEmail.getText().toString().trim()
            var password = inpPassword.getText().toString().trim()

            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this){task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Login Sucessfull", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, DashActivity::class.java))
                    }else {
                        Toast.makeText(
                            this, "Login Failed, Email" +
                                    " or Password doesn't match", Toast.LENGTH_LONG).show()
                    }
                }
        }

        createAccount.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }

    }
}