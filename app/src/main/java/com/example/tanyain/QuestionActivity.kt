package com.example.tanyain

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class QuestionActivity : AppCompatActivity() {

    private lateinit var category: TextInputEditText
    private lateinit var problemDesc: TextInputEditText
    private lateinit var btnImage: Button
    private lateinit var btnUpload: Button
    private lateinit var profileImg: ImageView

    private lateinit var database: DatabaseReference
    private lateinit var progressBar: ProgressBar

    private var maxid: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        category = findViewById(R.id.category)
        problemDesc = findViewById(R.id.problemDesc)
        btnImage = findViewById(R.id.btnAddImg)
        btnUpload = findViewById(R.id.btnbrowse)
        profileImg = findViewById(R.id.questionImg)
        progressBar = findViewById(R.id.pb_Question)

        var uid: String = FirebaseAuth.getInstance().currentUser!!.uid
        database = FirebaseDatabase.getInstance().getReference("tanyain").child("question")

        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                maxid = snapshot.childrenCount
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        btnImage.setOnClickListener{
            getContent.launch("image/*")
        }

        btnUpload.setOnClickListener{
            progressBar.visibility = View.VISIBLE
            val filename = UUID.randomUUID().toString()
            val img_database = FirebaseStorage.getInstance().getReference("/question_images/$filename")
            var category = category.text.toString().trim()
            var desc = problemDesc.text.toString().trim()
            FirebaseDatabase.getInstance().getReference("tanyain").child("users").child(uid).get()
                .addOnSuccessListener {
                    var fName = it.child("firstName").value.toString().trim()
                    var lName = it.child("lastName").value.toString().trim()
                    val id= maxid.plus(1).toString().trim()
                    Log.d("URL:", it.toString())

                    img_database.putFile(selectedPhoto!!)
                        .addOnSuccessListener {
                            img_database.downloadUrl.addOnSuccessListener {
                                var url = it.toString()
                                val question = Question( uid,fName+" "+lName,"Unsolved",category,desc,url)
                                database.child(id).setValue(question).addOnCompleteListener(this) { task ->
                                    if(task.isSuccessful){
                                        progressBar.visibility = View.GONE
                                        Toast.makeText(this,"Problem posted successfully!",Toast.LENGTH_SHORT).show()
                                        startActivity(Intent(this,DashActivity::class.java))
                                    }else {
                                        progressBar.visibility = View.GONE
                                        Toast.makeText(this,"Problem posted Failed! Try Again",Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                        .addOnFailureListener{
                            progressBar.visibility = View.GONE
                            Toast.makeText(this,"Image failed to upload!",Toast.LENGTH_SHORT).show()
                        }
                }
        }
    }
    var selectedPhoto: Uri? = null

    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri!=null){
            btnImage.alpha = 0f
            profileImg.setImageURI(uri)
            selectedPhoto = uri
        }
    }

}

