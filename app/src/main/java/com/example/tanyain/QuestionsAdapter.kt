package com.example.tanyain

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QuestionsAdapter (private val questionList : ArrayList<Question>) : RecyclerView.Adapter<QuestionsAdapter.DataVHold>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionsAdapter.DataVHold {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.questions, parent,false)

        return DataVHold(itemView)
    }

    override fun onBindViewHolder(holder: QuestionsAdapter.DataVHold, position: Int) {

        val currentitem = questionList[position]

        holder.nama.text = currentitem.name
        holder.category.text = currentitem.kategori
        holder.question.text = currentitem.desc

    }

    override fun getItemCount(): Int {

        return questionList.size
    }

    class DataVHold(itemView : View) : RecyclerView.ViewHolder(itemView){


        val nama : TextView = itemView.findViewById(R.id.rv_Name)
        val category : TextView = itemView.findViewById(R.id.rv_Category)
        val question : TextView = itemView.findViewById(R.id.rv_Question)

    }
}