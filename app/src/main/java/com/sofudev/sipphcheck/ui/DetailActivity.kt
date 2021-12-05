package com.sofudev.sipphcheck.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.archit.calendardaterangepicker.customviews.CalendarListener
import com.sofudev.sipphcheck.BaseActivity
import com.sofudev.sipphcheck.R
import com.sofudev.sipphcheck.adapter.DataInputAdapter
import com.sofudev.sipphcheck.dialog.NewDetailDialog
import com.sofudev.sipphcheck.model.DataInput
import com.sofudev.sipphcheck.session.PrefManager
import com.sofudev.sipphcheck.utils.DateConvert
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.dialog_date_range.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class DetailActivity : BaseActivity() {
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
        loading.visibility = View.VISIBLE

        val layoutManager = LinearLayoutManager(this)
        recyclerview.layoutManager = layoutManager
        recyclerview.setHasFixedSize(true)
        recyclerview.adapter = adapter

        btn_filter.setOnClickListener {
            showDialog()
        }
    }

    override fun initEvents() {
        prefManager = PrefManager(this)
//        checkData()
    }

    override fun onResume() {
        super.onResume()
        checkData()
    }

    override fun getLayoutId(): Int = R.layout.activity_detail

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_date_range)

        var stDate : Calendar? = null
        var edDate : Calendar? = null

        dialog.calendar.setCalendarListener(object : CalendarListener {
            @SuppressLint("SimpleDateFormat")
            override fun onDateRangeSelected(startDate: Calendar, endDate: Calendar) {
                stDate = startDate
                edDate = endDate
            }

            override fun onFirstDateSelected(startDate: Calendar) {

            }
        })

        dialog.btn_ok.setOnClickListener {
            if (stDate == null && edDate == null)
            {
                Toast.makeText(applicationContext, "Harap pilih tanggal", Toast.LENGTH_SHORT).show()
            }
            else
            {
                val converter = DateConvert()

                val startDate = converter.convertCalendarToDate(stDate!!)
                val endDate = converter.convertCalendarToDate(edDate!!)

                filterData(prefManager.getIdUser(), startDate, endDate)
                loading.visibility = View.VISIBLE
                recyclerview.visibility = View.GONE
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun checkData(){
        dataList.clear()
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
                                prefManager.getUsername()!!,
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

                loading.visibility = View.GONE
                adapter.notifyDataSetChanged()
            },
            { error ->
                error.printStackTrace()
            })

        requestQueue.cache.clear()
        requestQueue.add(jsonObjectRequest)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterData(idUser: String?, stDate: String, edDate: String){
        dataList.clear()
        val url = "https://timurrayalab.com/salesapi/Input/filterData"

        val requestQueue = Volley.newRequestQueue(this)

        val postData = JSONObject()
        try {
            postData.put("id_user", idUser)
            postData.put("start_date", stDate)
            postData.put("end_date", edDate)
            println("IdUser : $idUser")
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
                        img_notfound.visibility = View.VISIBLE
                        recyclerview.visibility = View.GONE
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                loading.visibility = View.GONE
                recyclerview.visibility = View.VISIBLE
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
}