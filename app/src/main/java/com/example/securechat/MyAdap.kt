package com.example.securechat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import android.opengl.Visibility
import androidx.recyclerview.widget.RecyclerView
import com.example.securechat.util.desc

class MyAdap(private val chatList:ArrayList<Chat>):RecyclerView.Adapter<MyAdap.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemview = LayoutInflater.from(parent.context).inflate(R.layout.chat_item,parent,false)
        return MyViewHolder(itemview)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem =chatList[position]

        holder.key.text=currentitem.key
        holder.value.text=currentitem.value





        holder.decryptionBtn.setOnClickListener{
            val value1=currentitem.value
            val decryptionion=desc()
            val value2=decryptionion.decryption(value1.toString())

            holder.value.text=value2

        }
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    class MyViewHolder(itemView : View):RecyclerView.ViewHolder(itemView){
        val key:TextView=itemView.findViewById(R.id.key)
        val value:TextView=itemView.findViewById(R.id.value)
        val decryptionBtn:ImageButton=itemView.findViewById(R.id.decryption_btn)


    }
}