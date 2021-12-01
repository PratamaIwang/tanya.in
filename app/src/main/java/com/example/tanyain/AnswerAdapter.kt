package com.example.tanyain

import android.content.Context
import android.graphics.BitmapFactory
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class AnswerAdapter (private val answerList : ArrayList<Answer>, context: Context) : RecyclerView.Adapter<AnswerAdapter.AnswerVHold>() {

    class AnswerVHold(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val nama : TextView = itemView.findViewById(R.id.rvAnswer_Name)
        val answer : TextView = itemView.findViewById(R.id.rvAnswer_AnswerDesc)
        val answerImg : ImageView = itemView.findViewById(R.id.rvAnswer_Image)
        val profileImg : ImageView = itemView.findViewById(R.id.rvAnswer_ProfileImg)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerVHold {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.answers, parent,false)

        return AnswerVHold(itemView)
    }

    override fun onBindViewHolder(holder: AnswerVHold, position: Int) {
        val currentitem = answerList[position]
        val uid = currentitem.userId
        val questionId = currentitem.getQuestionId
        val answerIdString = currentitem.getAnswerId
        val answerId = answerIdString?.toLong()?.plus(1)

        var user_database = FirebaseDatabase.getInstance().getReference("tanyain").child("users")
        user_database.child(uid.toString()).get().addOnSuccessListener {
            var filename = it.child("imageId").value.toString().trim()
            val database_store = FirebaseStorage.getInstance().reference.child("users_images/$filename")
            val localfile = File.createTempFile("tempImage","jpg")
            database_store.getFile(localfile).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                holder.profileImg.setImageBitmap(bitmap)
            }
        }

        var question_database = FirebaseDatabase.getInstance().getReference("tanyain").child("question")
        question_database.child(questionId.toString()).child("answer").child(answerId.toString()).get().addOnSuccessListener {
            var filename = it.child("answerImageUUID").value.toString().trim()
            if (filename!="null"){
                holder.answerImg.visibility = View.VISIBLE
                Log.i("test", filename)
                val database_store = FirebaseStorage.getInstance().reference.child("answers_images/$filename")
                val localfile = File.createTempFile("tempImage","jpg")
                database_store.getFile(localfile).addOnSuccessListener {
                    val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                    holder.answerImg.setImageBitmap(bitmap)
                }
            }else{
                holder.answerImg.visibility = View.GONE
            }
        }


        holder.nama.text = currentitem.name
        holder.answer.text = currentitem.answerDesc

    }

    override fun getItemCount(): Int {
        return answerList.size
    }

}