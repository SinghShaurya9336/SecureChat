package com.example.securechat



public fun encryption_Algo(x:String ) {
    val a=x;
    val rough: MutableList<Int> = mutableListOf()        // or, use `arrayListOf`
    for(i in a.indices){
        val x1=a[i].toInt()
        rough.add(x1)
//
    }
    print(rough)
}

