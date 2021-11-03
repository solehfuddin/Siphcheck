package com.sofudev.sipphcheck.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sofudev.sipphcheck.BaseActivity
import com.sofudev.sipphcheck.R
import com.sofudev.sipphcheck.adapter.ColorAdapter
import com.sofudev.sipphcheck.adapter.DataInputAdapter
import com.sofudev.sipphcheck.model.DataInput
import com.sofudev.sipphcheck.model.UserColor
import com.sofudev.sipphcheck.session.PrefManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject

class MainActivity : BaseActivity() {
    private lateinit var prefManager: PrefManager
    private val recyclerView: RecyclerView = findViewById(R.id.recyclerview)

    override fun initControls(savedInstanceState: Bundle?) {
        loading.visibility = View.GONE
    }

    override fun initEvents() {
        prefManager = PrefManager(this)
        checkLogin()
        checkData()

        fab_camera.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
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

    private fun checkData(){
        val url = "https://timurrayalab.com/salesapi/Input/getData"

        val requestQueue = Volley.newRequestQueue(this)

        val postData = JSONObject()
        try {
            postData.put("id_user", prefManager.getIdUser())
            println("IdUser : ${prefManager.getIdUser()}")
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            url,
            postData,
            {
                try {
                    println(it)
                    val code = it.getInt("code")

                    if (code == 200)
                    {
                        val jsonArray = it.getJSONArray("data")

                    }
                    else
                    {
                        img_notfound.visibility = View.VISIBLE
                        recyclerview.visibility = View.GONE
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

    private fun logoutApp() {
        prefManager.removeData()
        btn_logout.isEnabled = true
        loading.visibility = View.GONE
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}