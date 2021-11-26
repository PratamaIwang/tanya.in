package com.example.tanyain

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Personaldata : AppCompatActivity() {

    private lateinit var inpfName: EditText
    private lateinit var inplName: EditText
    private lateinit var inpBirthday: EditText
    private lateinit var inpAddress: EditText
    private lateinit var inpNoHP: EditText
    private lateinit var inpRadioGroup: RadioGroup
    private lateinit var inpGender: RadioButton
    private lateinit var btnSave: Button

    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personaldata)

        inpfName = findViewById(R.id.inpFname)
        inplName = findViewById(R.id.inpLname)
        inpBirthday = findViewById(R.id.inpBirth)
        inpAddress = findViewById(R.id.inpAddr)
        inpNoHP = findViewById(R.id.inpNo)
        btnSave = findViewById(R.id.btnSave)

        database = FirebaseDatabase.getInstance().getReference("tanyain").child("users")

        btnSave.setOnClickListener{
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

            val uid: String = FirebaseAuth.getInstance().currentUser!!.uid

            database.child(uid).child("firstName").setValue(fName)
            database.child(uid).child("lastName").setValue(lName)
            database.child(uid).child("birthday").setValue(birthday)
            database.child(uid).child("address").setValue(address)
            database.child(uid).child("noHP").setValue(noHP)
            database.child(uid).child("gender").setValue(gender).addOnCompleteListener(this){ task ->
                if (task.isSuccessful){
                    inpfName.setText("")
                    inplName.setText("")
                    inpBirthday.setText("")
                    inpAddress.setText("")
                    inpNoHP.setText("")
                    Toast.makeText(this, "Data Saved!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, DashActivity::class.java))
                }else{
                    Toast.makeText(this, "Data not saved! Try again", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}