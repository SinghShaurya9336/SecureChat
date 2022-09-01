package com.example.securechat

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.*
import com.example.securechat.databinding.ActivityMessageSendBinding
import com.example.securechat.util.SharedPref
import com.example.securechat.util.ency
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar
import kotlin.math.abs


class MessageSendActivity : AppCompatActivity(), GestureDetector.OnGestureListener {

    private lateinit var binding:ActivityMessageSendBinding
    private lateinit var database : DatabaseReference
    lateinit var gestureDetector: GestureDetector
    var x2:Float = 0.0f
    var x1:Float = 0.0f

    companion object{
        const val MIN_DISTANCE = 100
    }
    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dateTime: String
        val calendar:Calendar
        val simpleDateFormat:SimpleDateFormat
        //val roomName= intent.getStringExtra("roomId")
        val roomName= SharedPref(this).getGroupname()



        binding= ActivityMessageSendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.encryptBtn.setOnClickListener{
            val value1= binding.value?.text.toString()
            val cyText=findViewById<TextView>(R.id.cypher_text_view)
            val encryptinon=ency()
            val Value=encryptinon.encryption(value1.toString())

            cyText.text=Value
        }
        calendar= Calendar.getInstance()
        simpleDateFormat= SimpleDateFormat("dd.MM.yyyy HH:mm:ss aaa z")
        dateTime=simpleDateFormat.format(calendar.time).toString()

        binding.send?.setOnClickListener{
            val x1=calendar.timeInMillis.toString()
            var key= SharedPref(this).getUserName()
            if (key.length>15){
                val x2=x1.slice((x1.length-4) until x1.length)
                    key= ("Anonymous $x2")
            }
            else{
                key=key.slice(0..(key.length-1))
            }
            val Value= binding.value?.text.toString()
            val encryptinon=ency()
            val Value1=encryptinon.encryption(Value.toString())



            database=FirebaseDatabase.getInstance().getReference("Chats/$roomName")
            if (roomName != null) {
                val data = mutableMapOf<String,Any>()
                data["key"] = key
                data["value"] = Value1

                database.push().setValue(data).addOnSuccessListener {
                    Toast.makeText(this,"Successful",Toast.LENGTH_SHORT).show()
                    binding.value?.text?.clear()

                }.addOnFailureListener{
                    Toast.makeText(this, it.message,Toast.LENGTH_SHORT).show()

                }
            }


        }


        gestureDetector = GestureDetector(this, this)
        val historyBtn=findViewById<Button>(R.id.show)
        historyBtn.setOnClickListener {
            val intent = Intent(this, HistoryPage::class.java)
            startActivity(intent)
        }

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        gestureDetector.onTouchEvent(event)

        when(event?.action){


            //At the starting of swipe
            0->
            {
                x1 = event.x
            }

            //At the end of swipe
            1->
            {
                x2 = event.x

                val valueX:Float = x2-x1

                if(abs(valueX) > MIN_DISTANCE){
                    //condition for right swipe
                    if(x2 < x1){
                        startActivity(Intent(this, HistoryPage::class.java))
                        finish()
                    }
                }
            }
        }

        return super.onTouchEvent(event)
    }





    override fun onDown(p0: MotionEvent?): Boolean {
        return false;
    }

    override fun onShowPress(p0: MotionEvent?) {

    }

    override fun onSingleTapUp(p0: MotionEvent?): Boolean {
        return false;
    }

    override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        return false;
    }

    override fun onLongPress(p0: MotionEvent?) {

    }

    override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        return false;
    }




}