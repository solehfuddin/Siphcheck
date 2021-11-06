package com.sofudev.sipphcheck.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.sofudev.sipphcheck.R
import kotlinx.android.synthetic.main.dialog_date_range.*
import android.widget.Toast

import com.sofudev.sipphcheck.ui.MainActivity

import com.archit.calendardaterangepicker.customviews.CalendarListener
import java.util.*


class DateRangeDialog (
    context: Context
) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_date_range)
        setTitle(context.resources.getString(R.string.your_color))

        calendar.setCalendarListener(object : CalendarListener {
            override fun onDateRangeSelected(startDate: Calendar, endDate: Calendar) {
                Toast.makeText(
                    context,
                    "Start Date: " + startDate.getTime().toString()
                        .toString() + " End date: " + endDate.getTime().toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onFirstDateSelected(startDate: Calendar) {

            }
        })

        btn_ok.setOnClickListener {
            dismiss()
        }
    }

}