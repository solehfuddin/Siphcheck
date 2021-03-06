package com.sofudev.sipphcheck.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sofudev.sipphcheck.BaseActivity
import com.sofudev.sipphcheck.R
import com.sofudev.sipphcheck.adapter.DataInputAdapter
import com.sofudev.sipphcheck.dialog.NewDetailDialog
import com.sofudev.sipphcheck.model.DataInput
import com.sofudev.sipphcheck.session.PrefManager
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.loading
import kotlinx.android.synthetic.main.activity_main.recyclerview
import org.json.JSONException
import org.json.JSONObject

class MainActivity : BaseActivity() {
    private lateinit var prefManager: PrefManager
    private val dataList : ArrayList<DataInput> = ArrayList()
    private val onItemClick: (DataInput) -> Unit = {
        val detailDialog = NewDetailDialog(this, it, deleteItem)
        detailDialog.show()
    }
    private val deleteItem: (DataInput) -> Unit = {
        deleteData(it.idInput)
    }
    private val adapter = DataInputAdapter(this, dataList, onItemClick)

    override fun initControls(savedInstanceState: Bundle?) {
        loading.visibility = View.GONE

        val layoutManager = LinearLayoutManager(this)
        recyclerview.layoutManager = layoutManager
        recyclerview.setHasFixedSize(true)
        recyclerview.adapter = adapter
    }

    override fun initEvents() {
        prefManager = PrefManager(this)
        checkLogin()
//        checkData()

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

    override fun onResume() {
        super.onResume()
        checkData()
    }

    override fun getLayoutId(): Int = R.layout.activity_main

    private fun checkLogin(){
        if (!prefManager.isLogin()){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun checkData(){
        dataList.clear()
        val url = "https://timurrayalab.com/salesapi/Input/getNewData"

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
                                prefManager.getUsername()!!,
                                `object`.getString("kode_warna"),
                                `object`.getString("kode_ph"),
                                `object`.getString("kategori_ph"),
                                `object`.getString("tgl_input")
                            )

                            dataList.add(dataInput)
                        }
                    } else {
                        imgnotfound.visibility = View.VISIBLE
                        recyclerview.visibility = View.GONE
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                loadingdata.visibility = View.GONE
                adapter.notifyDataSetChanged()
            },
            { error ->
                error.printStackTrace()
            })

        requestQueue.cache.clear()
        requestQueue.add(jsonObjectRequest)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun deleteData(id: String){
        val url = "https://timurrayalab.com/salesapi/Input/deleteData"

        val requestQueue = Volley.newRequestQueue(this)

        val postData = JSONObject()
        try {
            postData.put("id_input", id)
            println("id_input : $id")
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
                        Toast.makeText(this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Data gagal dihapus", Toast.LENGTH_SHORT).show()
                    }

                    checkData()
                } catch (e: JSONException) {
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