package com.manuelvelez.taskly.features.taskly.domain.usecases

import com.manuelvelez.taskly.features.taskly.domain.entities.Task
import com.manuelvelez.taskly.features.taskly.domain.repositories.TaskRepository

class UpdateTaskUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(task: Task) {
        repository.updateTask(task)
    }
}