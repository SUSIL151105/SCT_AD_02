package com.example.sct_ad_02


data class Todo(
    val id: Int,
    var title: String,
    var isCompleted: Boolean = false
)