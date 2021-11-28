package com.example.tanyain

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class QuestionViewModel : ViewModel () {
    private val listQuestion = mutableListOf<QuestionsModel>()
    val MainLiveQuestion = MutableLiveData<List<QuestionsModel>>()
    init {
        MainLiveQuestion.value = listQuestion
    }
    fun getQuestion() : List<QuestionsModel> {
        return listQuestion
    }
    fun addQuestion(nama :String, tanggal: String, kategori:String, pertanyaan:String ){
        listQuestion.add(QuestionsModel(nama, tanggal, kategori, pertanyaan,))
        MainLiveQuestion.value = listQuestion
    }
}