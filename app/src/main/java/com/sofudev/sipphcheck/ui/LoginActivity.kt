package com.sofudev.sipphcheck.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.sofudev.sipphcheck.BaseActivity
import com.sofudev.sipphcheck.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {


    override fun initControls(savedInstanceState: Bundle?) {

    }

    override fun initEvents() {
        button_login.setOnClickListener {
            val username = edit_username.text.toString().trim()
            val password = edit_password.text.toString().trim()

            if (username == "demo" && password == "demo123"){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            else
            {
                Toast.makeText(this, "Password Salah", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_login
}