package com.example.tanyain

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.system.Os.close
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
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
    private lateinit var btnMenu: ImageView

    private lateinit var header: View
    private lateinit var nav_Name: TextView
    private lateinit var nav_Email: TextView

    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: DatabaseReference

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        dispName = findViewById(R.id.nameProfile)
        dispGender = findViewById(R.id.genderProfile)
        dispEmail = findViewById(R.id.emailProfile)

        btnMenu = findViewById(R.id.menuBtn)
        btnEdit = findViewById(R.id.btnEditProfile)

        mAuth = Firebase.auth
        database = FirebaseDatabase.getInstance().getReference("tanyain")

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val nav_view: NavigationView = findViewById(R.id.nav_view)

        btnMenu.setOnClickListener{
            if(!drawerLayout.isDrawerOpen(GravityCompat.START)){
                drawerLayout.openDrawer(Gravity.START)
            }
        }

        nav_view.setNavigationItemSelectedListener {
            var id = it.itemId
            if (id==R.id.nav_home){
                startActivity(Intent(this,DashActivity::class.java))
            }else if(id==R.id.nav_Profile){
                startActivity(Intent(this,Profile::class.java))
            }else if(id==R.id.nav_Logout){
                mAuth.signOut()
                startActivity(Intent(this,LoginActivity::class.java))
            }
            true
        }


        /*Read Data From Firebase Realtime Database*/
        var uid: String = FirebaseAuth.getInstance().currentUser!!.uid
        database.child("users").child(uid).get()
            .addOnSuccessListener {
            var fName = it.child("firstName").value.toString().trim()
            var lName = it.child("lastName").value.toString().trim()
            var gender = it.child("gender").value.toString().trim()
            var email = it.child("email").value.toString().trim()

            dispName.setText(fName.plus(" ").plus(lName))
            dispGender.setText(gender)
            dispEmail.setText(email)
        }

        header=nav_view.getHeaderView(0)

        if(uid!=null){
            nav_Name = header.findViewById(R.id.nav_nameProfile)
            nav_Email = header.findViewById(R.id.nav_emailProfile)
            database.child("users").child(uid).get()
                .addOnSuccessListener {
                    var fName = it.child("firstName").value.toString().trim()
                    var lName = it.child("lastName").value.toString().trim()
                    var email = it.child("email").value.toString().trim()

                    nav_Name.setText(fName.plus(" ").plus(lName))
                    nav_Email.setText(email)
                }
        }
    }

}

