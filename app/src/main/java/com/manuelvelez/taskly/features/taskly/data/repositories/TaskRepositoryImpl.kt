package com.manuelvelez.taskly.features.taskly.data.repositories

import com.manuelvelez.taskly.features.taskly.data.datasources.remote.api.TaskApi
import com.manuelvelez.taskly.features.taskly.data.datasources.remote.mapper.toDomain
import com.manuelvelez.taskly.features.taskly.data.datasources.remote.mapper.toDto
import com.manuelvelez.taskly.features.taskly.domain.entities.Task
import com.manuelvelez.taskly.features.taskly.domain.repositories.TaskRepository
class TaskRepositoryImpl(
    private val api: TaskApi
) : TaskRepository {

    override suspend fun getTasks(): List<Task> {
        val response = api.getTasks()
        return response.map { it.toDomain() }
    }
    override suspend fun createTask(task: Task) {
        val dto = task.toDto()
        api.createTask(dto)
    }
    override suspend fun updateTask(task: Task) {
        val dto = task.toDto()
        api.updateTask(task.id, dto)
    }
    override suspend fun deleteTask(taskId: String) {
        api.deleteTask(taskId)
    }
}