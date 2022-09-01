package com.example.securechat.util

import android.annotation.SuppressLint
import android.content.Context

private const val EMAIL = "email"
private const val Gkey = "groupkey"

class SharedPref(val context: Context) {

    fun setUsername(username: String) {
        val preference = context.getSharedPreferences("Pref", Context.MODE_PRIVATE)
        val editor = preference.edit()
        editor.putString(EMAIL, username)
        editor.apply()
    }

    fun getUserName(): String {
        val preference = context.getSharedPreferences("Pref", Context.MODE_PRIVATE)
        return preference.getString(EMAIL, "") ?: ""
    }
    fun setGroupname(Groupname: String) {
        val preference = context.getSharedPreferences("Pref", Context.MODE_PRIVATE)
        val editor = preference.edit()
        editor.putString(Gkey, Groupname)
        editor.apply()
    }
    fun getGroupname(): String {
        val preference = context.getSharedPreferences("Pref", Context.MODE_PRIVATE)
        return preference.getString(Gkey, "") ?: ""
    }

    @SuppressLint("CommitPrefEdits")
    fun logout() {
        val preference = context.getSharedPreferences("Pref", Context.MODE_PRIVATE)
        preference.edit().clear()
    }
}