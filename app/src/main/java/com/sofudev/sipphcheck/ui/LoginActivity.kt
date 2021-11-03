package com.sofudev.sipphcheck.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sofudev.sipphcheck.BaseActivity
import com.sofudev.sipphcheck.R
import com.sofudev.sipphcheck.session.PrefManager
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : BaseActivity() {

    private lateinit var prefManager: PrefManager

    override fun initControls(savedInstanceState: Bundle?) {
        loading.visibility = View.GONE
    }

    override fun initEvents() {
        prefManager = PrefManager(this)
        checkLogin()

        button_login.setOnClickListener {
            loading.visibility = View.VISIBLE
            button_login.isEnabled = false

            val urlLogin = "https://timurrayalab.com/salesapi/User/loginUser"
            val username = edit_username.text.toString().trim()
            val password = edit_password.text.toString().trim()

            val requestQueue = Volley.newRequestQueue(this)

            val postData = JSONObject()
            try {
                postData.put("username", username)
                postData.put("password", password)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST,
                urlLogin,
                postData,
                {
                    loading.visibility = View.GONE
                    button_login.isEnabled = true

                    try {
                        println(it)
                        val code = it.getInt("code")

                        if (code == 200)
                        {
                            val intent = Intent(this, MainActivity::class.java)
                            val userName = it.getString("username")
                            val idUser = it.getString("id_user")
                            prefManager.setLogin(true)
                            prefManager.setUsername(userName)
                            prefManager.setIdUser(idUser)

                            startActivity(intent)
                        }
                        else
                        {
                            Toast.makeText(this, "Password Salah", Toast.LENGTH_SHORT).show()
                        }
                    }catch (e: JSONException){
                        e.printStackTrace()
                    }
                },
                { error ->
                    error.printStackTrace()
                })

            requestQueue.cache.clear()
            requestQueue.add(jsonObjectRequest)
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_login

    private fun checkLogin(){
        if (prefManager.isLogin()){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}