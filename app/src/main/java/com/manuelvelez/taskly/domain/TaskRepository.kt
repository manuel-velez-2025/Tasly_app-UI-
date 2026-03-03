package com.manuelvelez.taskly.domain

interface TaskRepository {
    suspend fun getTasks(): List<Task>
    suspend fun getTaskById(id: String): Task
    suspend fun createTask(task: Task): Task
    suspend fun updateTask(id: String, task: Task): Task
    suspend fun deleteTask(id: String)
}