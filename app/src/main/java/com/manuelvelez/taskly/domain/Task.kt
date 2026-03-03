package com.manuelvelez.taskly.domain

data class Task(
    val id: String = "",
    val title: String,
    val description: String,
    val subject: String,
    val date: String,
    val isCompleted: Boolean
)