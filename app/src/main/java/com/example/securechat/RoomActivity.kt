package com.example.securechat

import android.content.Intent
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.airbnb.lottie.LottieAnimationView
import com.example.securechat.util.SharedPref
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase




class RoomActivity : AppCompatActivity() {
    private lateinit var database : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)
        val auth= FirebaseAuth.getInstance()

        // Code Start Here
        var user_Name=intent.getStringExtra("intent_value")

        findViewById<TextView>(R.id.user_textView).text= SharedPref(this).getUserName()


val animat=findViewById<LottieAnimationView>(R.id.animate)
val roomButton=findViewById<ImageButton>(R.id.to_send_message_button)
        val roomNo=findViewById<EditText>(R.id.group_key_edit_text)


        roomButton.setOnClickListener{
            roomButton.visibility = View.GONE
            animat.visibility = View.VISIBLE
            database=FirebaseDatabase.getInstance().getReference("Chats")
            database.get().addOnSuccessListener {
                val roomName = roomNo.text.toString()
                if(it.child(roomName).exists()){

                    Handler().postDelayed({

                        Toast.makeText(this,"Room Found Successfully !",Toast.LENGTH_SHORT).show()
                        SharedPref(this).setGroupname(roomName)
                        launchChatWindow(roomName)
                        roomButton.visibility = View.VISIBLE
                        animat.visibility = View.GONE
                    },1000)


                } else {

                    Handler().postDelayed({
                    roomButton.visibility = View.VISIBLE
                    animat.visibility = View.GONE
                    Toast.makeText(this,"Room Doesn't Exist",Toast.LENGTH_SHORT).show()
                                          },1000)


                }

            }

        }

    }

    private fun launchChatWindow(roomNo: String) {
        val intent = Intent(this, MessageSendActivity::class.java)
        intent.putExtra("roomId", roomNo)
        startActivity(intent)
        this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)

    }

    fun toMessageSend(view: View){
        startActivity(Intent(this, MessageSendActivity::class.java))
        this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }
    fun toCreateKey(view: View){
       startActivity(Intent(this,  CreateKey::class.java))
        this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

}