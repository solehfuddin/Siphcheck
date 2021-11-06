package com.sofudev.sipphcheck.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sofudev.sipphcheck.BaseActivity
import com.sofudev.sipphcheck.R
import com.sofudev.sipphcheck.session.PrefManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    private lateinit var prefManager: PrefManager

    override fun initControls(savedInstanceState: Bundle?) {
        loading.visibility = View.GONE
    }

    override fun initEvents() {
        prefManager = PrefManager(this)
        checkLogin()

        btn_cekph.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }

        btn_hasilpH.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java)
            startActivity(intent)
        }

        btn_logout.setOnClickListener {
            loading.visibility = View.VISIBLE
            btn_logout.isEnabled = false

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
        btn_logout.isEnabled = true
        loading.visibility = View.GONE
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}