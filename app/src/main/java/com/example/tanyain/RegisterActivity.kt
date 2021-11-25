package com.example.tanyain

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class RegisterActivity : AppCompatActivity() {

    private lateinit var inpRegUsername: EditText
    private lateinit var inpRegPassword: EditText
    private lateinit var inpConfPassword: EditText
    private lateinit var register: TextView
    private lateinit var BtnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val dbHelper = DBHelper(this);

        inpRegUsername = findViewById(R.id.inpUsernameReg)
        inpRegPassword = findViewById(R.id.inpPasswordReg)
        inpConfPassword = findViewById(R.id.inpConfPassword)
        BtnRegister = findViewById(R.id.btnRegister)

        register = findViewById(R.id.tvRegister)

        register.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }

        BtnRegister.setOnClickListener{
            var username :String = inpRegUsername.getText().toString().trim()
            var password :String = inpRegPassword.getText().toString().trim()
            var confPassword :String = inpConfPassword.getText().toString().trim()

            var values :ContentValues = ContentValues()

            if (password.equals(confPassword)){
                values.put(DBHelper.row_username, username)
                values.put(DBHelper.row_password, password)
                dbHelper.inserData(values)

                Toast.makeText(this,"Register Successfull", Toast.LENGTH_SHORT).show()
                finish()
            }else if (password.equals("")|| username.equals("")){
                Toast.makeText(this,"Username or Password cant be empty", Toast.LENGTH_SHORT).show()
            }else {
                Toast.makeText(this,"Password not match", Toast.LENGTH_SHORT).show()
            }
        }
    }
}