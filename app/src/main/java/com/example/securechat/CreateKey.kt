package com.example.securechat

import android.content.Intent
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlin.system.exitProcess

class CreateKey : AppCompatActivity() {
    private lateinit var database : DatabaseReference
    lateinit var roomButton: Button
    lateinit var roomNo: EditText
    lateinit var pageAnimation: LottieAnimationView
    lateinit var createKeyText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_key)


        roomButton = findViewById(R.id.create_room)
        roomNo = findViewById(R.id.room_no)
        val keyAnimation = findViewById<LottieAnimationView>(R.id.key_animation)
        pageAnimation = findViewById(R.id.image)
        createKeyText = findViewById(R.id.textView3)

        roomButton.setOnClickListener {

            visiblityGone()
            keyAnimation.visibility = View.VISIBLE

            database = FirebaseDatabase.getInstance().getReference("Chats")
            database.get().addOnSuccessListener {

                val roomName = roomNo.text.toString()
                if (roomName.isEmpty()){

                    keyAnimation.visibility = View.GONE
                    visiblityYes()
                    Toast.makeText(this, "Room Name Cannot be Empty", Toast.LENGTH_SHORT).show()

                }
                else if (it.child(roomName).exists()) {

                    keyAnimation.visibility = View.GONE
                    visiblityYes()
                    Toast.makeText(this, "Already created Room Try New one", Toast.LENGTH_SHORT).show()
                } else {



                    database.child(roomName).setValue("Hello User").addOnSuccessListener {
                        Toast.makeText(this, "Room Created Successfully !", Toast.LENGTH_SHORT).show()
                        launchChatWindow(roomName)

                    }.addOnFailureListener{
                        Toast.makeText(this, "Error !", Toast.LENGTH_SHORT).show()

                    }
                }

            }
        }


    }
    fun visiblityGone(){
        createKeyText.visibility = View.GONE
        pageAnimation.visibility = View.GONE
        roomButton.visibility = View.GONE
        roomNo.visibility= View.GONE
    }
    fun visiblityYes(){
        createKeyText.visibility = View.VISIBLE
        pageAnimation.visibility = View.VISIBLE
        roomButton.visibility = View.VISIBLE
        roomNo.visibility= View.VISIBLE
    }

        private fun launchChatWindow(roomNo: String) {
            val intent = Intent(this, RoomActivity::class.java)
            intent.putExtra("roomId", roomNo)
            startActivity(intent)

    }
}