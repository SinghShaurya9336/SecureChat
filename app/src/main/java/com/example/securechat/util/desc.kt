package com.example.securechat.util


class desc() {
    fun decryption(a: String): String {
        val l= a.toCharArray()
        val l1= mutableListOf<Char>()
        val shift =10
        val shift2=10
        var i=0
        if ((l[0].toInt())-shift<32){
            while (i<(l.size-1)){
                val x=l[i].toInt()
                val y=1*(127-32)+x-shift
                l1.add(y.toChar())
                i+=shift2


            }


        }
        else{
            while (i<(l.size-1)){
                val x=l[i].toInt()
                val y=0*(127-32)+x-shift
                l1.add(y.toChar())
                i+=shift2

            }

        }
        val s2=l1.joinToString("")
        return s2
    }

}