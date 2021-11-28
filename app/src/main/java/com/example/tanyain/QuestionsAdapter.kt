package com.example.tanyain

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QuestionsAdapter (var data : List<QuestionsModel>) :

    RecyclerView.Adapter<QuestionsAdapter.DataVHold>() {
    inner class DataVHold(itemView: View) :

        RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataVHold {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.questions, parent, false)
        return DataVHold(view)
    }

    override fun onBindViewHolder(holder: DataVHold, position: Int) {
        holder.itemView.apply {
//            findViewById<ImageView>(R.id.fotoProfil).image = data[position].fotoProfil
            findViewById<TextView>(R.id.name).text = data[position].nama
            findViewById<TextView>(R.id.date).text = data[position].tanggal
            findViewById<TextView>(R.id.category).text = data[position].kategori
            findViewById<TextView>(R.id.ques).text = data[position].pertanyaan
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}