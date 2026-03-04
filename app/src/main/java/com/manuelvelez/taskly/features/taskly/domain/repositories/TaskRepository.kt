package com.manuelvelez.taskly.features.taskly.domain.repositories

import com.manuelvelez.taskly.features.taskly.domain.entities.Task

interface TaskRepository {
    suspend fun getTasks(): List<Task>
    suspend fun createTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(taskId: String)
}