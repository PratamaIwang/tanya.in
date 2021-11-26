package com.example.tanyain

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


class RegisterActivity : AppCompatActivity() {

    private lateinit var inpEmailReg: EditText
    private lateinit var inpPasswordReg: EditText
    private lateinit var inpConfPassword: EditText
    private lateinit var register: TextView
    private lateinit var btnRegister: Button
    private lateinit var btLogin: TextView

    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        inpEmailReg = findViewById(R.id.inpEmailReg)
        inpPasswordReg = findViewById(R.id.inpPasswordReg)
        inpConfPassword = findViewById(R.id.inpConfPassword)
        register = findViewById(R.id.tvRegister)
        btnRegister = findViewById(R.id.btnRegister)
        progressBar = findViewById(R.id.pb_register)
        btLogin = findViewById(R.id.tvRegister)

        mAuth = Firebase.auth
        database = FirebaseDatabase.getInstance().getReference("tanyain")

        btnRegister.setOnClickListener {
            var email = inpEmailReg.text.toString().trim()
            var password = inpPasswordReg.text.toString().trim()
            var passwordconf = inpConfPassword.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "Email is required!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                Toast.makeText(this, "Password is required!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (passwordconf.isEmpty()) {
                Toast.makeText(this, "Password confirmation is required!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                Toast.makeText(this, "Please provide valid email!", Toast.LENGTH_SHORT).show()
            }
            if (password.length < 8) {
                Toast.makeText(
                    this,
                    "Min password length should be 8 characters!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        progressBar.visibility = View.VISIBLE
                        val uid: String = FirebaseAuth.getInstance().currentUser!!.uid
                        val user = User(email, password, null, null, null, null, null, null)
                        database.child("users").child(uid).setValue(user)
                            .addOnCompleteListener(this) { task ->
                                if (task.isSuccessful) {
                                    progressBar.visibility = View.GONE
                                    Toast.makeText(this, "Register Successfull!", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this, Personaldata::class.java))
                                } else {
                                    progressBar.visibility = View.GONE
                                    Toast.makeText(this, "Register failed! Try again", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        progressBar.visibility = View.GONE
                        Toast.makeText(this, "Register failed! Try again", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        btLogin.setOnClickListener{
            startActivity(Intent(this,LoginActivity::class.java))
        }
    }
}