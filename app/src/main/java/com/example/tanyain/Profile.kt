package com.example.tanyain

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlin.math.log

class Profile : AppCompatActivity() {

    private lateinit var dispName: TextView
    private lateinit var dispGender: TextView
    private lateinit var dispEmail: TextView

    private lateinit var btnEdit: Button

    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        dispName = findViewById(R.id.nameProfile)
        dispGender = findViewById(R.id.genderProfile)
        dispEmail = findViewById(R.id.emailProfile)

        btnEdit = findViewById(R.id.btnEditProfile)

        mAuth = Firebase.auth
        database = FirebaseDatabase.getInstance().getReference("tanyain")

        var uid: String = FirebaseAuth.getInstance().currentUser!!.uid
        database.child("users").child(uid).get().addOnSuccessListener {
            var fName = it.child("firstName").value.toString().trim()
            var lName = it.child("lastName").value.toString().trim()
            var gender = it.child("gender").value.toString().trim()
            var email = it.child("email").value.toString().trim()

            dispName.setText(fName.plus(" ").plus(lName))
            dispGender.setText(gender)
            dispEmail.setText(email)
        }
    }
}