package com.sofudev.sipphcheck.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.sofudev.sipphcheck.R
import com.sofudev.sipphcheck.adapter.ColorAdapter
import com.sofudev.sipphcheck.database.ColorViewModel
import kotlinx.android.synthetic.main.dialog_color.*

class ColorDialog(
    context: Context,
    private val colorViewModel: ColorViewModel,
    private val colorAdapter: ColorAdapter,
    private val onClearColor: () -> Unit
) : Dialog(context) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_color)

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rv_color.layoutManager = layoutManager
        rv_color.setHasFixedSize(true)
        rv_color.adapter = colorAdapter

        btn_add_color.setOnClickListener {
            val name = edt_name_of_list.text.toString()

            if (name.isNotEmpty()) {
                colorAdapter.colors.forEach {
                    it.name = name
                    colorViewModel.insertColor(it)
                }
                //colorViewModel.insertAllColor(colorAdapter.colors)
                onClearColor()
                dismiss()
            }
        }

        btn_cancel.setOnClickListener {
            dismiss()
        }
    }
}