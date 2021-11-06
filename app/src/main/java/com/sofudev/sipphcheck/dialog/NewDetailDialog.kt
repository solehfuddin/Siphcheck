package com.sofudev.sipphcheck.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import com.sofudev.sipphcheck.R
import com.sofudev.sipphcheck.model.DataInput
import kotlinx.android.synthetic.main.dialog_color_detail.btn_cancel
import kotlinx.android.synthetic.main.dialog_color_detail.btn_remove_color
import kotlinx.android.synthetic.main.dialog_color_detail.txt_hex
import kotlinx.android.synthetic.main.dialog_color_detail.view_color_preview
import kotlinx.android.synthetic.main.dialog_new_detail.*

class NewDetailDialog (
    context: Context,
    private val dataInput: DataInput,
    private val onDelete: (DataInput) -> Unit
) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_new_detail)
        setTitle(context.resources.getString(R.string.your_color))

        view_color_preview.setBackgroundColor(Color.parseColor(dataInput.kodeWarna))

        txt_hex.text = ("Hex : ${dataInput.kodeWarna}")
        txt_kadar.text = ("Kadar pH : ${dataInput.kodePh}")
        txt_ket.text = ("Kategori pH : ${dataInput.kategoriPh}")

        btn_cancel.setOnClickListener { dismiss() }

        btn_remove_color.setOnClickListener {
            onDelete(dataInput)
            dismiss()
        }
    }
}