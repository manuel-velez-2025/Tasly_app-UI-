package com.manuelvelez.taskly.features.taskly.domain.usecases

import com.manuelvelez.taskly.features.taskly.domain.repositories.TaskRepository

class DeleteTaskUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(taskId: String) {
        repository.deleteTask(taskId)
    }
}