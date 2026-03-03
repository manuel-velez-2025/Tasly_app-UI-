package com.manuelvelez.taskly.presentation

import com.manuelvelez.taskly.domain.Task

data class TaskUiState(
    val isLoading: Boolean = false,
    val tasks: List<Task> = emptyList(),
    val error: String? = null
)