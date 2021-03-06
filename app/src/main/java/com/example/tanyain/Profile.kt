package com.example.tanyain

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
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
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import org.w3c.dom.Text
import java.io.File
import kotlin.math.log

class Profile : AppCompatActivity() {

    private lateinit var dispName: TextView
    private lateinit var dispGender: TextView
    private lateinit var dispEmail: TextView
    private lateinit var dispBirthday: TextView
    private lateinit var dispNoHP: TextView
    private lateinit var dispAddress: TextView
    private lateinit var totalAnswer: TextView
    private lateinit var totalQuestion: TextView

    private lateinit var btnEdit: Button

    private lateinit var btnMenu: ImageView
    private lateinit var profileImg: ImageView

    private lateinit var header: View
    private lateinit var nav_Name: TextView
    private lateinit var nav_Email: TextView
    private lateinit var nav_profilePic: ImageView

    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: DatabaseReference

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        dispName = findViewById(R.id.nameProfile)
        dispGender = findViewById(R.id.genderProfile)
        dispEmail = findViewById(R.id.emailProfile)
        dispAddress = findViewById(R.id.addressProfile)
        dispNoHP = findViewById(R.id.noHPProfile)
        dispBirthday = findViewById(R.id.birthdayProfile)

        totalAnswer = findViewById(R.id.profileAnswer)
        totalQuestion = findViewById(R.id.profileQuestion)

        btnMenu = findViewById(R.id.menuBtn)
        btnEdit = findViewById(R.id.btnEditProfile)

        profileImg = findViewById(R.id.imageProfile)

        mAuth = Firebase.auth
        database = FirebaseDatabase.getInstance().getReference("tanyain")

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val nav_view: NavigationView = findViewById(R.id.nav_view)

        var uid: String = FirebaseAuth.getInstance().currentUser!!.uid

        /*Navigation Menu*/
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
                finish()
            }
            true
        }

        header=nav_view.getHeaderView(0)

        if(uid!=null){
            nav_Name = header.findViewById(R.id.nav_nameProfile)
            nav_Email = header.findViewById(R.id.nav_emailProfile)
            nav_profilePic = header.findViewById(R.id.nav_imageProfile)
            database.child("users").child(uid).get()
                .addOnSuccessListener {
                    var fName = it.child("firstName").value.toString().trim()
                    var lName = it.child("lastName").value.toString().trim()
                    var email = it.child("email").value.toString().trim()
                    var filename = it.child("imageId").value.toString().trim()
                    val database_store = FirebaseStorage.getInstance().reference.child("users_images/$filename")
                    val localfile = File.createTempFile("tempImage","jpg")
                    database_store.getFile(localfile).addOnSuccessListener {
                        val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                        nav_profilePic.setImageBitmap(bitmap)
                    }
                    nav_Name.setText(fName.plus(" ").plus(lName))
                    nav_Email.setText(email)
                }
        }

        /*Read Data From Firebase Storage*/
        database.child("users").child(uid).get().addOnSuccessListener {
            var filename = it.child("imageId").value.toString().trim()
            val database_store = FirebaseStorage.getInstance().reference.child("users_images/$filename")
            val localfile = File.createTempFile("tempImage","jpg")
            database_store.getFile(localfile).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                profileImg.setImageBitmap(bitmap)
            }.addOnFailureListener{
                Toast.makeText(this,"Failed",Toast.LENGTH_LONG).show()
            }
        }

        /*Read Data From Firebase Realtime Database*/
        database.child("users").child(uid).get()
            .addOnSuccessListener {
                var fName = it.child("firstName").value.toString().trim()
                var lName = it.child("lastName").value.toString().trim()
                var gender = it.child("gender").value.toString().trim()
                var email = it.child("email").value.toString().trim()
                var birthday = it.child("birthday").value.toString().trim()
                var noHp = it.child("noHP").value.toString().trim()
                var address = it.child("address").value.toString().trim()

                var answer = it.child("totalAnswer").value.toString().trim()
                var question = it.child("totalQuestion").value.toString().trim()

                dispName.setText(fName.plus(" ").plus(lName))
                dispGender.setText(gender)
                dispEmail.setText(email)
                dispBirthday.setText(birthday)
                dispAddress.setText(address)
                dispNoHP.setText(noHp)

                totalQuestion.setText(question)
                totalAnswer.setText(answer)
            }

        /*Edit Profile Button*/
        btnEdit.setOnClickListener {
            startActivity(Intent(this, Personaldata::class.java))
        }
    }

}

