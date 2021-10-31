package com.sofudev.sipphcheck.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sofudev.sipphcheck.BaseActivity
import com.sofudev.sipphcheck.R
import com.sofudev.sipphcheck.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    override fun initControls(savedInstanceState: Bundle?) {
    }

    override fun initEvents() {
        btn_click.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_main
}