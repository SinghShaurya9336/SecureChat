package com.example.securechat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.securechat.util.SharedPref
import com.google.firebase.database.*
import kotlin.math.abs

class HistoryPage : AppCompatActivity(), GestureDetector.OnGestureListener {
    private lateinit var dbref: DatabaseReference
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var chatArrayList: ArrayList<Chat>

    lateinit var gestureDetector: GestureDetector
    var x2:Float = 0.0f
    var x1:Float = 0.0f

    companion object{
        const val MIN_DISTANCE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_page)


        gestureDetector = GestureDetector(this, this)

        chatRecyclerView=findViewById(R.id.chat_list)
        chatRecyclerView.layoutManager=LinearLayoutManager(this)
        chatRecyclerView.setHasFixedSize(true)



        chatArrayList= arrayListOf<Chat>()
        getChatData()
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
                    //Condition for left swipe
                    if(x2 > x1){
                        startActivity(Intent(this, MessageSendActivity::class.java))
                        finish()
                        this.overridePendingTransition(R.anim.left_right,R.anim.right_left)

                    }
                }
            }
        }

        return super.onTouchEvent(event)
    }






    override fun onDown(p0: MotionEvent?): Boolean {
        return false
    }

    override fun onShowPress(p0: MotionEvent?) {

    }

    override fun onSingleTapUp(p0: MotionEvent?): Boolean {
        return false
    }

    override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        return false
    }

    override fun onLongPress(p0: MotionEvent?) {

    }

    override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        return false
    }

    private fun getChatData() {
        val result= SharedPref(this).getGroupname()
        val roomId=intent.getStringExtra("roomId")
        dbref=FirebaseDatabase.getInstance().getReference("Chats/$result")
        dbref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (userSnapshot in snapshot.children){
                        val chat=userSnapshot.getValue(Chat::class.java)
                        chatArrayList.add(chat!!)

                    }
                    chatRecyclerView.adapter=MyAdap(chatArrayList)

                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun toMessageSend(){
        onBackPressed()
    }
}