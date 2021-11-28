package com.example.tanyain

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class Personaldata : AppCompatActivity() {

    private lateinit var inpfName: EditText
    private lateinit var inplName: EditText
    private lateinit var inpBirthday: EditText
    private lateinit var inpAddress: EditText
    private lateinit var inpNoHP: EditText
    private lateinit var inpRadioGroup: RadioGroup
    private lateinit var inpGender: RadioButton
    private lateinit var btnSave: Button
    private lateinit var btnImageAdd: Button
    private lateinit var profileImg: ImageView

    private lateinit var database: DatabaseReference

    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personaldata)

        /*Initialization*/
        inpfName = findViewById(R.id.inpFname)
        inplName = findViewById(R.id.inpLname)
        inpBirthday = findViewById(R.id.inpBirth)
        inpAddress = findViewById(R.id.inpAddr)
        inpNoHP = findViewById(R.id.inpNo)
        btnSave = findViewById(R.id.btnSave)
        btnImageAdd = findViewById(R.id.btnRegSelectImg)
        progressBar = findViewById(R.id.progressBarReg)
        profileImg = findViewById(R.id.reg_ProfileImg)

        val uid: String = FirebaseAuth.getInstance().currentUser!!.uid
        database = FirebaseDatabase.getInstance().getReference("tanyain").child("users")

        btnImageAdd.setOnClickListener{
            getContent.launch("image/*")
        }

        btnSave.setOnClickListener{
            progressBar.visibility = View.VISIBLE
            var fName = inpfName.getText().toString().trim()
            var lName = inplName.getText().toString().trim()
            var birthday = inpBirthday.getText().toString().trim()
            var address = inpAddress.getText().toString().trim()
            var noHP = inpNoHP.getText().toString().trim()

            /*Radio Button*/
            inpRadioGroup = findViewById(R.id.radioBtn)
            var selectedId: Int = inpRadioGroup.checkedRadioButtonId
            inpGender = findViewById(selectedId)
            var gender = inpGender.getText().toString().trim()

            /*Firebase*/
            val filename = UUID.randomUUID().toString()
            val img_database = FirebaseStorage.getInstance().getReference("/users_images/$filename")

            /*Firebase Image Update*/
            img_database.putFile(selectedPhoto!!)

            /*Firebase Data Update*/
            database.child(uid).child("firstName").setValue(fName)
            database.child(uid).child("lastName").setValue(lName)
            database.child(uid).child("birthday").setValue(birthday)
            database.child(uid).child("address").setValue(address)
            database.child(uid).child("noHP").setValue(noHP)
            database.child(uid).child("gender").setValue(gender)
            database.child(uid).child("imageId").setValue(filename)
                .addOnCompleteListener(this){ task ->
                if (task.isSuccessful){
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, "Data Saved!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, DashActivity::class.java))
                    finish()
                }else{
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, "Data not saved! Try again", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }
    var selectedPhoto: Uri? = null

    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri!=null){
            btnImageAdd.alpha = 0f
            profileImg.setImageURI(uri)
            selectedPhoto = uri
        }
    }
}