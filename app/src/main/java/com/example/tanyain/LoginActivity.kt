package com.example.tanyain

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html.fromHtml
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class LoginActivity : AppCompatActivity() {

    private lateinit var InpUsername: EditText
    private lateinit var InpPassword: EditText
    private lateinit var BtnLogin: Button
    private lateinit var createAccount: TextView


    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

//        InpUsername = findViewById(R.id.inpUsername)
//        InpPassword = findViewById(R.id.inpPassword)
//        BtnLogin = findViewById(R.id.btnLogin)
//        val dbHelper = DBHelper(this)
//        createAccount = findViewById(R.id.tvCreateAccount)
//
//        createAccount.setOnClickListener{
//            startActivity(Intent(this, RegisterActivity::class.java))
//        }
//
//        BtnLogin.setOnClickListener{
//            var username: String = InpUsername.getText().toString().trim()
//            var password: String = InpPassword.getText().toString().trim()
//
//            var res: Boolean = dbHelper.checkUser(username, password)
//            if (res == true){
//                Toast.makeText(this, "Login Successfull", Toast.LENGTH_SHORT).show()
//                startActivity(Intent(this, MainActivity::class.java))
//            }else{
//                Toast.makeText(this,"Login Failed", Toast.LENGTH_SHORT).show()
//            }
//        }
    }
}