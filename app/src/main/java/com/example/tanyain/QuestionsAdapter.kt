package com.example.tanyain

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.File


class QuestionsAdapter (private val questionList : ArrayList<Question>, context: Context) : RecyclerView.Adapter<QuestionsAdapter.DataVHold>() {

    private lateinit var database: DatabaseReference
    val mContext= context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataVHold {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.questions, parent,false)

        return DataVHold(itemView)
    }

    override fun onBindViewHolder(holder: DataVHold, position: Int) {

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

        holder.itemView.setOnClickListener {

            val intent = Intent(mContext,discussActivity::class.java)
            intent.putExtra("name", currentitem.name)
            intent.putExtra("category", currentitem.kategori)
            intent.putExtra("question", currentitem.desc)
            intent.putExtra("imageUUID", currentitem.imageUUID)
            intent.putExtra("userId", currentitem.userId)
            intent.putExtra("getId", currentitem.getId?.plus(1L).toString())

            mContext.startActivity(intent)

        }

    }
    override fun getItemCount(): Int {

        return questionList.size
    }

    class DataVHold(itemView: View) : RecyclerView.ViewHolder(itemView){


        val nama : TextView = itemView.findViewById(R.id.rv_Name)
        val category : TextView = itemView.findViewById(R.id.rv_Category)
        val question : TextView = itemView.findViewById(R.id.rv_Question)
        val profilePic : ImageView = itemView.findViewById(R.id.rv_ProfilePic)

    }

}