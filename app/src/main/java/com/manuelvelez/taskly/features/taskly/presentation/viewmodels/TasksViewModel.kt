package com.manuelvelez.taskly.features.taskly.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manuelvelez.taskly.features.taskly.domain.entities.Task
import com.manuelvelez.taskly.features.taskly.domain.usecases.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

class TasksViewModel(
    private val getTasksUseCase: GetTasksUseCase,
    private val createTaskUseCase: CreateTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(TasksUiState())
    val uiState: StateFlow<TasksUiState> = _uiState.asStateFlow()
    init {
        loadTasks()
    }
    fun loadTasks() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val result = getTasksUseCase()
                _uiState.update { it.copy(isLoading = false, tasks = result) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }
    fun selectTask(task: Task) {
        _uiState.update { it.copy(selectedTask = task) }
    }
    fun createTask(title: String, description: String, subject: String, date: String) {
        viewModelScope.launch {
            try {
                val newTask = Task(UUID.randomUUID().toString(), title, description, subject, date, false)
                createTaskUseCase(newTask)
                loadTasks()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Error al crear: ${e.message}") }
            }
        }
    }
    fun updateTask(task: Task) {
        viewModelScope.launch {
            try {
                updateTaskUseCase(task)
                loadTasks()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Error al actualizar: ${e.message}") }
            }
        }
    }
    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            try {
                deleteTaskUseCase(taskId)
                loadTasks()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Error al borrar: ${e.message}") }
            }
        }
    }

}