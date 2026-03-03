package com.manuelvelez.taskly.data

import com.manuelvelez.taskly.domain.Task
import com.manuelvelez.taskly.domain.TaskRepository

class TaskRepositoryImpl(private val api: TaskApi) : TaskRepository {
    override suspend fun getTasks(): List<Task> = api.getTasks()

    override suspend fun getTaskById(id: String): Task = api.getTaskById(id)

    override suspend fun createTask(task: Task): Task = api.createTask(task)

    override suspend fun updateTask(id: String, task: Task): Task = api.updateTask(id, task)

    override suspend fun deleteTask(id: String) = api.deleteTask(id)
}