package com.example.tanyain

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class Comment : AppCompatActivity() {

    private lateinit var answerDesc: EditText
    private lateinit var answerImage: ImageView

    private lateinit var addImgBtn: Button
    private lateinit var uploadAnswerBtn: Button

    private lateinit var database: DatabaseReference
    private lateinit var progressBar: ProgressBar

    private var maxid = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        answerDesc = findViewById(R.id.answerDesc)
        answerImage = findViewById(R.id.answerImg)

        addImgBtn = findViewById(R.id.answerImgAdd)
        uploadAnswerBtn = findViewById(R.id.uploadBtn)

        progressBar = findViewById(R.id.progressBarAddAnswer)

        /*Ambil Value Intent Extra*/
        val intent = intent
        val getId = intent.getStringExtra("getId")

        database = FirebaseDatabase.getInstance().getReference("tanyain").child("question")

        /*Button Listener*/
        addImgBtn.setOnClickListener{
            getContent.launch("image/*")
        }

        uploadAnswerBtn.setOnClickListener{
            val filename = UUID.randomUUID().toString()
            val uid = FirebaseAuth.getInstance().currentUser!!.uid

            val desc = answerDesc.text.toString().trim()

            if(desc.isEmpty()){
                Toast.makeText(this,"Please fill out the Answer Desc first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            progressBar.visibility = View.VISIBLE

            /*Database Instace*/
            val image_database = FirebaseStorage.getInstance().getReference("/answers_images$filename")
            FirebaseDatabase.getInstance().getReference("tanyain").child("users").child(uid).get()
                .addOnSuccessListener {
                    var fName = it.child("firstName").value.toString().trim()
                    var lName = it.child("lastName").value.toString().trim()
                    val id = getId

                    database.child(id.toString()).child("answer").addValueEventListener(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            maxid = snapshot.childrenCount
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }
                    })

                    if(selectedPhoto!=null){
                        image_database.putFile(selectedPhoto!!)
                        val answer = Answer(id.toString(),desc,filename,uid,fName+" "+lName)

                        database.child(id.toString()).child("answer").child(maxid.plus(1).toString().trim()).setValue(answer)
                            .addOnCompleteListener(this){ task ->
                                if(task.isSuccessful){
                                    progressBar.visibility = View.GONE
                                    Toast.makeText(this,"Answer uploaded successfully!",Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this,DashActivity::class.java))
                                    finish()
                                }else{
                                    progressBar.visibility = View.GONE
                                    Toast.makeText(this,"Answer upload failed! Try again",Toast.LENGTH_SHORT).show()
                                }
                            }
                    }else if(selectedPhoto==null){
                        val answer = Answer(id.toString(),desc,null,uid,fName+" "+lName)
                        database.child(id.toString()).child("answer").child(maxid.plus(1).toString().trim()).setValue(answer)
                            .addOnCompleteListener(this){ task ->
                                if(task.isSuccessful){
                                    progressBar.visibility = View.GONE
                                    Toast.makeText(this,"Answer uploaded successfully!",Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this,DashActivity::class.java))
                                    finish()
                                }else{
                                    progressBar.visibility = View.GONE
                                    Toast.makeText(this,"Answer upload failed! Try again",Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                }

        }
    }
    var selectedPhoto: Uri? = null

    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri!=null){
            addImgBtn.alpha = 0f
            answerImage.setImageURI(uri)
            selectedPhoto = uri
        }
    }
}