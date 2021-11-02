package com.sofudev.sipphcheck.ui

import android.content.Intent
import android.os.Bundle
import com.sofudev.sipphcheck.BaseActivity
import com.sofudev.sipphcheck.R
import com.sofudev.sipphcheck.session.PrefManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    private lateinit var prefManager: PrefManager

    override fun initControls(savedInstanceState: Bundle?) {
    }

    override fun initEvents() {
        prefManager = PrefManager(this)
        checkLogin()

        btn_click.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }

        btn_logout.setOnClickListener {
            logoutApp()
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_main

    private fun checkLogin(){
        if (!prefManager.isLogin()){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun logoutApp() {
        prefManager.removeData()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}