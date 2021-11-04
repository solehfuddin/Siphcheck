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
import com.sofudev.sipphcheck.adapter.ColorListAdapter
import com.sofudev.sipphcheck.adapter.DataInputAdapter
import com.sofudev.sipphcheck.dialog.ColorDetailDialog
import com.sofudev.sipphcheck.model.DataInput
import com.sofudev.sipphcheck.session.PrefManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_colors.*
import org.json.JSONException
import org.json.JSONObject

class MainActivity : BaseActivity() {
    private lateinit var prefManager: PrefManager
    private val dataList : ArrayList<DataInput> = ArrayList()
    private val adapter = DataInputAdapter(this, dataList)

    override fun initControls(savedInstanceState: Bundle?) {
        loading.visibility = View.GONE
    }

    override fun initEvents() {
        prefManager = PrefManager(this)
        checkLogin()
        checkData()

        val layoutManager = LinearLayoutManager(this)
        recyclerview.layoutManager = layoutManager
        recyclerview.setHasFixedSize(true)
        recyclerview.adapter = adapter

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

                    if (code == 200) {
                        val jsonArray = it.getJSONArray("data")
                        for (i in 0 until jsonArray.length()) {
                            val `object` = jsonArray.getJSONObject(i)
                            val dataInput = DataInput(
                                `object`.getString("id_input"),
                                `object`.getString("kode_warna"),
                                `object`.getString("kode_ph"),
                                `object`.getString("kategori_ph"),
                                `object`.getString("tgl_input")
                            )

                            dataList.add(dataInput)
                        }
                    } else {
                        img_notfound.visibility = View.VISIBLE
                        recyclerview.visibility = View.GONE
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                adapter.notifyDataSetChanged()
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