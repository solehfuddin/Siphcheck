package com.sofudev.sipphcheck.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.sofudev.sipphcheck.R
import com.sofudev.sipphcheck.model.DataInput
import com.sofudev.sipphcheck.model.UserColor
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DataInputAdapter(private val context: Context, private val dataInput: List<DataInput>,
                       private val onItemClick: (DataInput) -> Unit = {}) :
    RecyclerView.Adapter<DataInputAdapter.DataInputViewHolder>(){

    @SuppressLint("SetTextI18n")
    inner class DataInputViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val txtKadar: TextView = itemView.findViewById(R.id.txt_kadar)
//        private val txtKategori: TextView = itemView.findViewById(R.id.txt_kategori)
//        private val txtTanggal: TextView = itemView.findViewById(R.id.txt_tanggal)
//        private val viewKode: View = itemView.findViewById(R.id.view)
        private val txtKadar: TextView = itemView.findViewById(R.id.txt_kadar)
        private val txtNama: TextView = itemView.findViewById(R.id.txt_nama)
        private val txtTanggal: TextView = itemView.findViewById(R.id.txt_tanggal)
        private val layout: ConstraintLayout = itemView.findViewById(R.id.constraint)

        @SuppressLint("SimpleDateFormat")
        fun onBind(item: DataInput, onItemClick: (DataInput) -> Unit){
//            txtKadar.text = "Kadar pH : ${item.kodePh}"
//            txtKategori.text = "Kategori pH : ${item.kategoriPh}"
//            txtTanggal.text = item.tglInput
//            viewKode.setBackgroundColor(Color.parseColor(item.kodeWarna))

            var formattedDate : String? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val parsedDate = LocalDateTime.parse(item.tglInput, DateTimeFormatter.ISO_DATE_TIME)
                formattedDate = parsedDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
            } else {
                val parser =  SimpleDateFormat("yyyy-MM-dd")
                val formatter = SimpleDateFormat("dd-MM-yyyy")
                formattedDate = formatter.format(parser.parse(item.tglInput))
            }

            txtNama.text = item.namaUser
            txtKadar.text = item.kodePh
            txtTanggal.text = formattedDate

            layout.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataInputViewHolder {
//        val view = LayoutInflater.from(context).inflate(R.layout.data_input_item, parent, false)
        val view = LayoutInflater.from(context).inflate(R.layout.data_input_new, parent, false)
        return DataInputViewHolder(view)
    }

    override fun onBindViewHolder(holder: DataInputViewHolder, position: Int) {
        holder.onBind(dataInput[position], onItemClick)
    }

    override fun getItemCount(): Int = dataInput.size
}