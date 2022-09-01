package com.example.securechat.util

class ency() {
    fun encryption(chat:String): String {
        val l=chat.toCharArray()
        val shift=10
        val shift2=10
        val l1= mutableListOf<Int>()
        val l2= mutableListOf<Char>()
        for (i in 0..(l.size-1)){
            for (j in 0..shift2-1){
                val x=l[i].toInt()
                l1.add(32+(x-32+shift+j)%(127-32))
            }

        }
        for (i in 0..(l1.size-1))
        {
            l2.add(l1[i].toChar())
        }
        val s=l2.joinToString("")
        return s
    }

}
