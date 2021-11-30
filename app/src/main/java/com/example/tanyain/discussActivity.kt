package com.example.tanyain

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class discussActivity : AppCompatActivity() {

    private lateinit var nama: TextView
    private lateinit var kategori: TextView
    private lateinit var desc: TextView

    private lateinit var profileImg: ImageView
    private lateinit var questionImg: ImageView
    private lateinit var backButton: ImageView

    private lateinit var addAnswer: FloatingActionButton

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discuss)

        nama = findViewById(R.id.discuss_Name)
        kategori = findViewById(R.id.discuss_Category)
        desc = findViewById(R.id.discuss_QuestionDesc)

        backButton = findViewById(R.id.discuss_Backbtn)
        profileImg = findViewById(R.id.discuss_Profilepic)
        questionImg = findViewById(R.id.discuss_QuestionImg)

        addAnswer = findViewById(R.id.fabAnswer)

        /*Ambil Value Intent Extra*/
        val intent = intent
        val name = intent.getStringExtra("name")
        val category = intent.getStringExtra("category")
        val questionDesc = intent.getStringExtra("question")
        val imageUUID = intent.getStringExtra("imageUUID")
        val userId = intent.getStringExtra("userId")
        val getId = intent.getStringExtra("getId")

        /*Set Value Display*/
        nama.text = name
        kategori.text = category
        desc.text = questionDesc

        /*Add Answer On Click Listener*/
        addAnswer.setOnClickListener{
            val intent = Intent(this,Comment::class.java)
            intent.putExtra("getId", getId)
            startActivity(intent)
        }

        /*Firebase Storage Access*/
        /*User Image*/
        val uid = userId
        database = FirebaseDatabase.getInstance().getReference("tanyain").child("users")
        database.child(uid.toString()).get().addOnSuccessListener {
            var filename = it.child("imageId").value.toString().trim()
            val database_store = FirebaseStorage.getInstance().reference.child("users_images/$filename")
            val localfile = File.createTempFile("tempImage", "jpg")
            database_store.getFile(localfile).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                profileImg.setImageBitmap(bitmap)
            }
        }
        /*Question Image*/
        if (imageUUID != null) {
            questionImg.visibility = View.VISIBLE
            database = FirebaseDatabase.getInstance().getReference("tanyain").child("question")
            database.child(getId.toString()).get().addOnSuccessListener {
                var filename = it.child("imageUUID").value.toString().trim()
                val database_store = FirebaseStorage.getInstance().reference.child("questions_images/$filename")
                val localfile = File.createTempFile("tempImage_Question", "jpg")
                database_store.getFile(localfile).addOnSuccessListener {
                    val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                    questionImg.setImageBitmap(bitmap)
                }
            }
        }

        /*Back Button*/
        backButton.setOnClickListener{
            onBackPressed()
        }
    }
}