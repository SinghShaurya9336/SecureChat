package com.example.securechat

import android.annotation.SuppressLint
import android.os.Handler
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import com.example.securechat.util.SharedPref
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {
    companion object{
        private const val RC_SIGN_IN=120
    }

    private lateinit var googleSignInClient:GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val anonymousLogin=findViewById<ImageView>(R.id.anonymus_login)
        val googleLogin=findViewById<ImageView>(R.id.google_login)
        val loginEmail=findViewById<EditText>(R.id.loginEmail)
        val loginPassword=findViewById<EditText>(R.id.loginPassword)
        val loginButton=findViewById<Button>(R.id.loginButton)
        val loginAnimation = findViewById<LottieAnimationView>(R.id.login_animation)
        val auth=FirebaseAuth.getInstance()
        //
        val userName=intent.getStringExtra("intent_value")
        //
        if (auth.currentUser!= null){
            val intent=Intent(this,RoomActivity::class.java)
            startActivity(intent)
            finish()
        }

//        loginPassword.setOnTouchListener(@SuppressLint("ClickableViewAccessibility")
//        object :View.OnTouchListener {
//
//        }View.OnTouchListener())

        loginButton.setOnClickListener{
            loginButton.visibility = View.INVISIBLE
            loginAnimation.visibility = View.VISIBLE
            loginAnimation.setMinAndMaxProgress(0.0f,0.38f)

            val emailText = loginEmail.text.toString()
            val passwordText = loginPassword.text.toString()

            if (TextUtils.isEmpty(emailText) || TextUtils.isEmpty(passwordText)) {
                Toast.makeText(this, "Credentials cannot be Empty", Toast.LENGTH_SHORT).show()
                loginButton.visibility = View.VISIBLE
                loginAnimation.visibility = View.INVISIBLE
                return@setOnClickListener
            }
            auth.signInWithEmailAndPassword(emailText,passwordText)
                .addOnCompleteListener{
                    if (it.isSuccessful) {
                        loginAnimation.setMinAndMaxProgress(0.39f,0.5f)
                        loginAnimation.playAnimation()

                        Handler().postDelayed({

                            val username = emailText.split("@")[0]
                            SharedPref(this).setUsername(username)
                            //
                            intent=Intent(this, RoomActivity::class.java)
                            intent.putExtra("Intent_Value",userName)
                            startActivity(intent)
                            //
                            finish()

                        },1000)



                    }
                    else{

                        loginAnimation.setMinAndMaxProgress(0.8f,1.0f)
                        Toast.makeText(this,it.exception?.message,Toast.LENGTH_SHORT).show()
                        Handler().postDelayed({
                            loginButton.visibility = View.VISIBLE
                            loginAnimation.visibility = View.INVISIBLE
                        },2500)
                    }
                    }
                }


//--------------------Anonymously Login---------------------------------------
        anonymousLogin.setOnClickListener{

                auth.signInAnonymously().addOnSuccessListener {
                    it.user
                    SharedPref(this).setUsername(it.user?.displayName?: it.user.toString())
                    startActivity(Intent(this,RoomActivity::class.java))
                    this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                }
        }
//-----------------------------Google----------------------------------------
        // Configure Google Sign In

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id_auth))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        googleLogin.setOnClickListener{
            signIn()

        }

    }

//------------------------------------Facebook---------------------------------


//If User is New To App
    fun toRegister(view: View) {
        startActivity(Intent(this, RegisterActivity::class.java))
        this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

// After User Creates Id
    fun toLogin(view: android.view.View) {}
    override fun onBackPressed() {
        super.onBackPressed()
    }

    fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception=task.exception
            if (task.isSuccessful){
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately

                }
            }else{

                Toast.makeText(this, exception.toString(), Toast.LENGTH_SHORT).show()
            }

        }
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val auth=FirebaseAuth.getInstance()
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(this,"Signed In Successfully!",Toast.LENGTH_SHORT).show()
                    val intent=Intent(this,RoomActivity::class.java)
                    startActivity(intent)
                    finish()
                    this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this,"Error!",Toast.LENGTH_SHORT).show()


                }
            }
    }
}