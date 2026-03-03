package com.manuelvelez.taskly.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.manuelvelez.taskly.domain.Task
import com.manuelvelez.taskly.domain.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TasksViewModel(private val repository: TaskRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    init {
        loadTasks()
    }

    fun loadTasks() {
        viewModelScope.launch {
            _uiState.value = TaskUiState(isLoading = true)
            try {
                val result = repository.getTasks()
                _uiState.value = TaskUiState(tasks = result)
            } catch (e: Exception) {
                _uiState.value = TaskUiState(error = "Asegúrate de que Ktor esté encendido")
            }
        }
    }
    fun createTask(task: Task) {
        viewModelScope.launch {
            try {
                repository.createTask(task)
                loadTasks()
            } catch (e: Exception) {
                _uiState.value = TaskUiState(error = e.message)
            }
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            try {
                repository.updateTask(task.id, task)
                loadTasks()
            } catch (e: Exception) {
                _uiState.value = TaskUiState(error = e.message)
            }
        }
    }

    fun deleteTask(id: String) {
        viewModelScope.launch {
            try {
                repository.deleteTask(id)
                loadTasks()
            } catch (e: Exception) {
                _uiState.value = TaskUiState(error = e.message)
            }
        }
    }
}

class TasksViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TasksViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TasksViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel desconocido")
    }
}