package com.manuelvelez.taskly.features.taskly.presentation.viewmodels

import com.manuelvelez.taskly.features.taskly.domain.entities.Task
data class TasksUiState(
    val isLoading: Boolean = false,
    val tasks: List<Task> = emptyList(),
    val selectedTask: Task? = null,
    val errorMessage: String? = null
)