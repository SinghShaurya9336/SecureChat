package com.example.securechat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.securechat.util.SharedPref
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val email = findViewById<EditText>(R.id.email)
        val userName = findViewById<EditText>(R.id.user_name)
        val password = findViewById<EditText>(R.id.password)
        val confirmPassword = findViewById<EditText>(R.id.confirm_password)

        val signUpButton = findViewById<Button>(R.id.sign_up_button)

        signUpButton.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            val emailText = email.text.toString()
            val passwordText = password.text.toString()
            val confirmPasswordText = confirmPassword.text.toString()
            if (TextUtils.isEmpty(emailText)) {
                Toast.makeText(this, "Email cannot be Empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (TextUtils.isEmpty(passwordText)) {
                Toast.makeText(this, "Password cannot be Empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (TextUtils.isEmpty(confirmPasswordText)) {
                Toast.makeText(this, "Confirm Password cannot be Empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (passwordText.length < 6) {
                Toast.makeText(
                    this,
                    "Password should have at least 6 character",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            if (passwordText != confirmPasswordText) {
                Toast.makeText(this, "Password do not Match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            auth.createUserWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    } else {
                        Toast.makeText(this,task.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
            val user_Name=userName.text.toString()
            SharedPref(this).setUsername(user_Name)
            val intent=Intent(this,LoginActivity::class.java)
            intent.putExtra("intent_value",user_Name)
             startActivity(intent)

        }

    }


    fun toLogin(view: View) {
       onBackPressed()
       this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
   }
}