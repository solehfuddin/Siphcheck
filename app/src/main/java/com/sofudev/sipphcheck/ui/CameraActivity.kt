package com.sofudev.sipphcheck.ui

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sofudev.sipphcheck.BaseActivity
import com.sofudev.sipphcheck.R
import com.sofudev.sipphcheck.adapter.ColorAdapter
import com.sofudev.sipphcheck.dialog.ColorDetailDialog
import com.sofudev.sipphcheck.fragment.ColorsFragment
import com.sofudev.sipphcheck.handler.ColorDetectHandler
import com.sofudev.sipphcheck.model.MyColor
import com.sofudev.sipphcheck.model.UserColor
import com.sofudev.sipphcheck.session.PrefManager
import com.sofudev.sipphcheck.utils.FileDataPart
import com.sofudev.sipphcheck.utils.Fuzzy
import com.sofudev.sipphcheck.utils.VolleyFileUploadRequest
import com.sofudev.sipphcheck.utils.timer
import kotlinx.android.synthetic.main.activity_camera.*
import kotlinx.android.synthetic.main.activity_camera.txt_hex
import kotlinx.android.synthetic.main.dialog_color_detail.btn_cancel
import kotlinx.android.synthetic.main.dialog_color_detail.btn_remove_color
import kotlinx.android.synthetic.main.dialog_color_detail.view_color_preview
import kotlinx.android.synthetic.main.dialog_new_detail.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : BaseActivity() {
    private lateinit var prefManager: PrefManager

    companion object {
        private const val TAG = "CameraXBasic"
        private const val REQUEST_CODE_PERMISSIONS = 26
        private val REQUIRED_PERMISSIONS =
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
        private const val REQUEST_CODE = 112
    }

    private lateinit var cameraExecutor: ExecutorService

    private val cameraProviderFuture by lazy {
        ProcessCameraProvider.getInstance(this)
    }

    private val cameraProvider by lazy {
        cameraProviderFuture.get()
    }
    private var isBackCamera = true

    private var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    private val colorDetectHandler = ColorDetectHandler()

    private var timerTask: Job? = null

    private var currentColor = UserColor()

    private var isImageShown = false

    private var fuzzy : Fuzzy? = null

    private var imageData: ByteArray? = null

    private var dialogLoading : Dialog? = null

    private var currentColorList: MutableList<UserColor> =
        mutableListOf()

    private val colorAdapter: ColorAdapter by lazy {
        ColorAdapter(this) {
            val detailDialog = ColorDetailDialog(this, it, removeColorInList)
            detailDialog.show()
        }
    }

    private val colorsFragment: ColorsFragment by lazy {
        ColorsFragment()
    }

    override fun getLayoutId(): Int = R.layout.activity_camera

    override fun initControls(savedInstanceState: Bundle?) {
        if (allPermissionsGranted()) {

            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        cameraExecutor = Executors.newSingleThreadExecutor()

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_color.layoutManager = layoutManager
        rv_color.setHasFixedSize(true)
        rv_color.adapter = colorAdapter
    }


    override fun initEvents() {
        prefManager = PrefManager(this)
        btn_pick_color.setOnClickListener {
            addColor(isImageShown)
        }

        btn_pick_image.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE)
        }

        btn_change_camera.setOnClickListener {
            if (!isImageShown) {
                if (isBackCamera) {
                    cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
                    isBackCamera = false
                } else {
                    cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                    isBackCamera = true
                }
                startCamera()
            }
        }

        btn_show_camera.setOnClickListener {
            if (isImageShown) {
                btn_show_camera.visibility = View.GONE
                image_view.visibility = View.GONE
                isImageShown = false
                startCamera()
            }
        }

        btn_show_colors.setOnClickListener {
            showBottomSheetFragment()
        }
    }


    private fun startCamera() {

        cameraProviderFuture.addListener({

            timerTask?.cancel()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(camera_preview.createSurfaceProvider())
                }

            timerTask = CoroutineScope(Dispatchers.Default).timer(1000) {
                currentColor = colorDetectHandler.detect(camera_preview, pointer)
                Log.d(TAG, "Color : ${currentColor.hex}")

                withContext(Dispatchers.Main) {
                    txt_hex.text = currentColor.hex
                    card_color.setCardBackgroundColor(Color.parseColor(currentColor.hex))
                }
            }

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            val x = event.x

            val y = when {
                event.y < guideline_top.y -> guideline_top.y
                event.y > guideline_bottom1.y -> guideline_bottom1.y - pointer.height
                else -> event.y
            }

            setPointerCoordinates(x, y)
        }

        return super.onTouchEvent(event)
    }

    private fun setPointerCoordinates(x: Float, y: Float) {

        pointer.x = x
        pointer.y = y

        val marginBottom = this.resources.getDimension(R.dimen._20sdp)
        card_color_preview.y = y - marginBottom - pointer.height


        val cardColorPreviewX = when {
            x < guideline_left.x ->
                x
            x >= guideline_right.x -> {
                x - card_color_preview.width
            }
            else ->
                x - (card_color_preview.width / 2)
        }

        card_color_preview.x = cardColorPreviewX
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {

            if (!isImageShown) {
                image_view.visibility = View.VISIBLE
                btn_show_camera.visibility = View.VISIBLE
                isImageShown = true
            }
            if (data?.data != null) {
                image_view.setImageURI(data.data)
                createImageData(data.data!!)
                startDetectColorFromImage(decodeUriToBitmap(data.data!!))
            }
        }
    }

    private fun startDetectColorFromImage(bitmap: Bitmap) {

        cameraProvider.unbindAll()

        timerTask?.cancel()

        setPointerCoordinates(image_view.width / 2f, image_view.height / 2f)

        var isFitHorizontally = true

        var marginTop: Float = layout_top.height.toFloat()

        var marginLeft = 0f

        val ratio = if (bitmap.width >= bitmap.height) {
            bitmap.width / (image_view.width * 1.0f)
        } else {
            isFitHorizontally = false
            bitmap.height / (image_view.height * 1.0f)
        }


        if (isFitHorizontally) {
            marginTop += (image_view.height - bitmap.height / ratio) / 2
        } else {
            marginLeft += (image_view.width - bitmap.width / ratio) / 2
        }

        timerTask = CoroutineScope(Dispatchers.Default).timer(1000) {

            currentColor =
                colorDetectHandler.detect(bitmap, pointer, marginTop, marginLeft, ratio)
            Log.d(TAG, "Color : ${currentColor.hex}")

            withContext(Dispatchers.Main) {
                txt_hex.text = currentColor.hex
                card_color.setCardBackgroundColor(Color.parseColor(currentColor.hex))
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }


    private fun addColor(imageShow: Boolean) {
        try {
            //Check color before add to list
            Color.parseColor(currentColor.hex)

            colorDetectHandler.convertRgbToHsl(currentColor)
            currentColorList.add(0, currentColor)

            colorAdapter.notifyData(currentColorList)

            Log.d(CameraActivity::class.java.simpleName, "RGB(${currentColor.r}, ${currentColor.g}, ${currentColor.b})")
            fuzzy = Fuzzy()

            val getNear = fuzzy?.findNearest(MyColor(currentColor.r.toInt(), currentColor.g.toInt(), currentColor.b.toInt()))

            Log.d(TAG, "Nearest index : $getNear")
            val output = getNear.toString()
            val status = fuzzy?.checkStatus(getNear!!)

            if (imageShow)
            {
                showLoading()
                uploadImage(currentColor.hex, output, status)
            }
            else
            {
                insertData(currentColor.hex, output, status)
                showDialog(currentColor.hex, output, status)
            }

//            Toast.makeText(this, "RGB(${currentColor.r}, ${currentColor.g}, ${currentColor.b})", Toast.LENGTH_SHORT).show()
        } catch (e: IllegalArgumentException) {
            Toast.makeText(
                this,
                resources.getString(R.string.unknown_color),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showDialog(kodeWarna: String, kodePh: String?, katPh: String?) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_new_detail)

        dialog.view_color_preview.setBackgroundColor(Color.parseColor(kodeWarna))

        dialog.txt_hex.text = ("Hex : $kodeWarna")
        dialog.txt_kadar.text = ("Kadar pH : $kodePh")
        dialog.txt_ket.text = ("Kategori pH : $katPh")

        dialog.btn_remove_color.visibility = View.INVISIBLE

        dialog.btn_cancel.setOnClickListener {
            dialog.dismiss()
            finish()
        }

        dialog.show()
    }

    private fun showLoading() {
        dialogLoading = Dialog(this)
        dialogLoading!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogLoading!!.setCancelable(false)
        dialogLoading!!.setContentView(R.layout.dialog_loading)

        dialogLoading!!.btn_cancel.setOnClickListener {
            dialogLoading!!.dismiss()
            finish()
        }

        dialogLoading!!.show()
    }

    private fun hideLoading(){
        dialogLoading!!.dismiss()
    }

    private fun showBottomSheetFragment() {
        colorsFragment.show(supportFragmentManager, colorsFragment.tag)
    }

    private val removeColorInList: (UserColor) -> Unit = {
        currentColorList.remove(it)
        colorAdapter.notifyData(currentColorList)
    }

    override fun onDestroy() {
        super.onDestroy()
        timerTask?.cancel()
        cameraProvider.unbindAll()
        cameraExecutor.shutdown()
    }

    private fun decodeUriToBitmap(uri: Uri): Bitmap = try {
        val inputStream = contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
        e.printStackTrace()
        (image_view.drawable as BitmapDrawable).bitmap
    }

    @Throws(IOException::class)
    private fun createImageData(uri: Uri) {
        val inputStream = contentResolver.openInputStream(uri)
        inputStream?.buffered()?.use {
            imageData = it.readBytes()
        }
    }

    private fun insertData(kdWarna: String, kdPh: String?, katPh: String?){
        val url = "https://timurrayalab.com/salesapi/Input/insertData"

        val requestQueue = Volley.newRequestQueue(this)

        val postData = JSONObject()
        try {
            postData.put("id_user", prefManager.getIdUser())
            postData.put("kode_warna", kdWarna)
            postData.put("kode_ph", kdPh)
            postData.put("kategori_ph", katPh)
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
                        Toast.makeText(this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show()
                    }
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

    private fun uploadImage(kdWarna: String, kdPh: String?, katPh: String?) {
        val url = "https://timurrayalab.com/salesapi/Input/uploadData"

        val unique = System.currentTimeMillis()/1000

        imageData?: return
        val request = object : VolleyFileUploadRequest(
            Method.POST,
            url,
            Response.Listener {
                Log.d(CameraActivity::class.java.simpleName, "response is: $it")
                hideLoading()
                showDialog(currentColor.hex, kdPh, katPh)
            },
            Response.ErrorListener {
                it.printStackTrace()
                hideLoading()
            }
        ) {
            override fun getByteData(): MutableMap<String, FileDataPart> {
                val params = HashMap<String, FileDataPart>()
                params["imageFile"] = FileDataPart("$unique.jpg", imageData!!, "jpg")
                return params
            }

            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["id_user"] = prefManager.getIdUser().toString()
                params["kode_warna"] = kdWarna
                params["kode_ph"] = kdPh!!
                params["kategori_ph"] = katPh!!
                return params
            }
        }
        Volley.newRequestQueue(this).add(request)
    }
}