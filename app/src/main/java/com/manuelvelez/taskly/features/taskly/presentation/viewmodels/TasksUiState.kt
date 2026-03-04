package com.manuelvelez.taskly.features.taskly.presentation.viewmodels

import com.manuelvelez.taskly.features.taskly.domain.entities.Task

data class TasksUiState(
    val isLoading: Boolean = false,
    val tasks: List<Task> = emptyList(),
    val errorMessage: String? = null,
    val selectedTask: Task? = null,
    val formTitle: String = "",
    val formDescription: String = "",
    val formSubject: String = "",
    val formDate: String = "",
    val formIsCompleted: Boolean = false
)