package com.sofudev.sipphcheck.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sofudev.sipphcheck.R
import com.sofudev.sipphcheck.model.DataInput

class DataInputAdapter(private val context: Context, private val dataInput: List<DataInput>) :
    RecyclerView.Adapter<DataInputAdapter.DataInputViewHolder>(){

    @SuppressLint("SetTextI18n")
    inner class DataInputViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtKadar: TextView = itemView.findViewById(R.id.txt_kadar)
        private val txtKategori: TextView = itemView.findViewById(R.id.txt_kategori)
        private val txtTanggal: TextView = itemView.findViewById(R.id.txt_tanggal)
        private val viewKode: View = itemView.findViewById(R.id.view)

        fun onBind(item: DataInput){
            txtKadar.text = "Kadar pH : ${item.kodePh}"
            txtKategori.text = "Kategori pH : ${item.kategoriPh}"
            txtTanggal.text = item.tglInput
            viewKode.setBackgroundColor(Color.parseColor(item.kodeWarna))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataInputViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.data_input_item, parent, false)
        return DataInputViewHolder(view)
    }

    override fun onBindViewHolder(holder: DataInputViewHolder, position: Int) {
        holder.onBind(dataInput[position])
    }

    override fun getItemCount(): Int = dataInput.size
}