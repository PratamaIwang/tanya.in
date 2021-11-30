package com.example.tanyain

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class DashActivity : AppCompatActivity() {

    private lateinit var btnMenu: ImageView

    private lateinit var header: View
    private lateinit var nav_Name: TextView
    private lateinit var nav_Email: TextView
    private lateinit var nav_profilePic: ImageView

    private lateinit var fab: FloatingActionButton

    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var question_db: DatabaseReference

    private lateinit var recyclerView: RecyclerView
    private lateinit var question: ArrayList<Question>

    private var backPressedTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash)

        /*Initialization*/
        btnMenu = findViewById(R.id.menuBtn)
        val drawerLayout: DrawerLayout = findViewById(R.id.dash_drawerLayout)
        val nav_view: NavigationView = findViewById(R.id.dash_navView)

        var uid: String = FirebaseAuth.getInstance().currentUser!!.uid
        database = FirebaseDatabase.getInstance().getReference("tanyain")
        mAuth = Firebase.auth

        recyclerView = findViewById(R.id.rv_listQuestion)

        /*Recycler View*/
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        question = arrayListOf<Question>()
        getQuestionData()

        /*Floating Button*/
        fab = findViewById(R.id.fab1)
        fab.setOnClickListener{
            startActivity(Intent(this,QuestionActivity::class.java))
        }

        /*Side Navbar*/
        btnMenu.setOnClickListener{
            if(!drawerLayout.isDrawerOpen(GravityCompat.START)){
                drawerLayout.openDrawer(GravityCompat.START)
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

    }
    /*Get Question Data*/
    private fun getQuestionData() {
        question_db = FirebaseDatabase.getInstance().getReference("tanyain").child("question")

        question_db.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for(questionSnapshot in snapshot.children){
                        val question_data = questionSnapshot.getValue(Question::class.java)
                        question.add(question_data!!)
                    }
                    recyclerView.adapter = QuestionsAdapter(question,this@DashActivity)
                }
            }override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    /*Back Button BackEnd*/
    override fun onBackPressed() {
        if(backPressedTime + 2000 > System.currentTimeMillis()){
            super.onBackPressed()
        }else{
            Toast.makeText(applicationContext,"Press back again to exit app", Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()

    }
}