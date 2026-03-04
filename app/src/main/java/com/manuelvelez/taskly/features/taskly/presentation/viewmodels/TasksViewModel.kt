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
    fun onTitleChange(text: String) {
        _uiState.update { it.copy(formTitle = text) }
    }
    fun onDescriptionChange(text: String) {
        _uiState.update { it.copy(formDescription = text) }
    }
    fun onSubjectChange(text: String) {
        _uiState.update { it.copy(formSubject = text) }
    }
    fun onDateChange(text: String) {
        _uiState.update { it.copy(formDate = text) }
    }
    fun onIsCompletedChange(isChecked: Boolean) {
        _uiState.update { it.copy(formIsCompleted = isChecked) }
    }
    fun resetForm() {
        _uiState.update {
            it.copy(
                formTitle = "",
                formDescription = "",
                formSubject = "",
                formDate = "",
                formIsCompleted = false,
                errorMessage = null
            )
        }
    }
    fun initializeEditForm() {
        val task = _uiState.value.selectedTask
        task?.let { t ->
            _uiState.update {
                it.copy(
                    formTitle = t.title,
                    formDescription = t.description,
                    formSubject = t.subject,
                    formDate = t.date,
                    formIsCompleted = t.isCompleted
                )
            }
        }
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

    fun saveNewTask() {
        val currentState = _uiState.value

        if (currentState.formTitle.isBlank() || currentState.formSubject.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Título y materia requeridos") }
            return
        }

        viewModelScope.launch {
            try {
                val newTask = Task(
                    id = UUID.randomUUID().toString(),
                    title = currentState.formTitle,
                    description = currentState.formDescription,
                    subject = currentState.formSubject,
                    date = currentState.formDate,
                    isCompleted = currentState.formIsCompleted
                )
                createTaskUseCase(newTask)
                loadTasks()
                resetForm()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Error al crear: ${e.message}") }
            }
        }
    }

    fun saveEditedTask() {
        val currentState = _uiState.value
        val taskToEdit = currentState.selectedTask ?: return

        viewModelScope.launch {
            try {
                val updatedTask = taskToEdit.copy(
                    title = currentState.formTitle,
                    description = currentState.formDescription,
                    subject = currentState.formSubject,
                    date = currentState.formDate,
                    isCompleted = currentState.formIsCompleted
                )
                updateTaskUseCase(updatedTask)
                loadTasks()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Error al actualizar: ${e.message}") }
            }
        }
    }
    fun updateTaskStatus(task: Task) {
        viewModelScope.launch {
            try {
                updateTaskUseCase(task)
                loadTasks()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Error status: ${e.message}") }
            }
        }
    }
    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            try {
                deleteTaskUseCase(taskId)
                loadTasks()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Error borrar: ${e.message}") }
            }
        }
    }
}