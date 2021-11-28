package com.example.tanyain

import android.graphics.BitmapFactory
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.File


class QuestionsAdapter (private val questionList : ArrayList<Question>) : RecyclerView.Adapter<QuestionsAdapter.DataVHold>() {

    private lateinit var database: DatabaseReference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionsAdapter.DataVHold {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.questions, parent,false)

        return DataVHold(itemView)
    }

    override fun onBindViewHolder(holder: QuestionsAdapter.DataVHold, position: Int) {

        val currentitem = questionList[position]
        val uid = currentitem.userId

        database = FirebaseDatabase.getInstance().getReference("tanyain").child("users")
        database.child(uid.toString()).get().addOnSuccessListener {
            var filename = it.child("imageId").value.toString().trim()
            Log.i("imageid", filename)
            val database_store = FirebaseStorage.getInstance().reference.child("users_images/$filename")
            val localfile = File.createTempFile("tempImage","jpg")
            database_store.getFile(localfile).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                holder.profilePic.setImageBitmap(bitmap)
            }
        }

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
        val profilePic : ImageView = itemView.findViewById(R.id.rv_ProfilePic)

    }
}