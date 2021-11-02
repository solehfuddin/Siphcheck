package com.sofudev.sipphcheck.session

import android.content.Context
import android.content.SharedPreferences

class PrefManager(var context: Context) {
    val PRIVATE_MODE = 0

    private val PREF_NAME = "sharedPref"
    private val IS_LOGIN = "is_login"
    private val USERNAME = "username"
    private val IDUSER   = "iduser"
    private val empty = ""

    var pref : SharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    var editor : SharedPreferences.Editor = pref.edit()

    fun setLogin(isLogin: Boolean){
        editor.putBoolean(IS_LOGIN, isLogin)
        editor.apply()
    }

    fun setUsername(user: String){
        editor.putString(USERNAME, user)
        editor.apply()
    }

    fun setIdUser(id: String){
        editor.putString(IDUSER, id)
        editor.apply()
    }

    fun isLogin() : Boolean{
        return pref.getBoolean(IS_LOGIN, false)
    }

    fun getUsername() : String? {
        return pref.getString(USERNAME, empty)
    }

    fun getIdUser(): String? {
        return pref.getString(IDUSER, empty)
    }

    fun removeData(){
        editor.clear()
        editor.apply()
    }
}