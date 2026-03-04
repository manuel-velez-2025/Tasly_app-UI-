package com.manuelvelez.taskly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.manuelvelez.taskly.core.network.RetrofitClient
import com.manuelvelez.taskly.core.ui.TasklyTheme
import com.manuelvelez.taskly.features.taskly.data.repositories.TaskRepositoryImpl
import com.manuelvelez.taskly.features.taskly.domain.usecases.CreateTaskUseCase
import com.manuelvelez.taskly.features.taskly.domain.usecases.DeleteTaskUseCase
import com.manuelvelez.taskly.features.taskly.domain.usecases.GetTasksUseCase
import com.manuelvelez.taskly.features.taskly.domain.usecases.UpdateTaskUseCase
import com.manuelvelez.taskly.features.taskly.presentation.screens.TaskCreateScreen
import com.manuelvelez.taskly.features.taskly.presentation.screens.TaskDetailScreen
import com.manuelvelez.taskly.features.taskly.presentation.screens.TaskEditScreen
import com.manuelvelez.taskly.features.taskly.presentation.screens.TaskListScreen
import com.manuelvelez.taskly.features.taskly.presentation.viewmodels.TasksViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TasklyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // ---------------------------------------------------------
                    // 1. INYECCIÓN DE DEPENDENCIAS (Manual)
                    // ---------------------------------------------------------

                    // A. Cliente API y Repositorio
                    val api = RetrofitClient.api
                    val repository = TaskRepositoryImpl(api)

                    // B. Casos de Uso (Lógica de Negocio)
                    val getTasksUseCase = GetTasksUseCase(repository)
                    val createTaskUseCase = CreateTaskUseCase(repository)
                    val updateTaskUseCase = UpdateTaskUseCase(repository)
                    val deleteTaskUseCase = DeleteTaskUseCase(repository)

                    // C. Fábrica del ViewModel
                    val factory = TasksViewModelFactory(
                        getTasksUseCase,
                        createTaskUseCase,
                        updateTaskUseCase,
                        deleteTaskUseCase
                    )

                    // D. El ViewModel listo para usarse
                    val viewModel: TasksViewModel = viewModel(factory = factory)


                    // ---------------------------------------------------------
                    // 2. NAVEGACIÓN
                    // ---------------------------------------------------------
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "list") {

                        // RUTA 1: Lista de Tareas
                        composable("list") {
                            Scaffold(
                                floatingActionButton = {
                                    FloatingActionButton(onClick = { navController.navigate("create") }) {
                                        Icon(Icons.Default.Add, contentDescription = "Crear")
                                    }
                                }
                            ) { padding ->
                                Surface(modifier = Modifier.padding(padding)) {
                                    TaskListScreen(
                                        viewModel = viewModel,
                                        onNavigateToDetail = { task ->
                                            viewModel.selectTask(task) // Guardar selección
                                            navController.navigate("detail") // Ir al detalle
                                        }
                                    )
                                }
                            }
                        }

                        // RUTA 2: Crear Tarea
                        composable("create") {
                            TaskCreateScreen(
                                viewModel = viewModel,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }

                        // RUTA 3: Detalle de Tarea (Ver / Borrar)
                        composable("detail") {
                            TaskDetailScreen(
                                viewModel = viewModel,
                                onNavigateToEdit = { navController.navigate("edit") },
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }

                        // RUTA 4: Editar Tarea
                        composable("edit") {
                            TaskEditScreen(
                                viewModel = viewModel,
                                onNavigateBack = {
                                    navController.popBackStack() // Cierra editar
                                    navController.popBackStack() // Cierra detalle (opcional)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

// =========================================================================
// CLASE FACTORY (Obligatoria para Manual DI)
// =========================================================================
class TasksViewModelFactory(
    private val getTasksUseCase: GetTasksUseCase,
    private val createTaskUseCase: CreateTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TasksViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TasksViewModel(
                getTasksUseCase,
                createTaskUseCase,
                updateTaskUseCase,
                deleteTaskUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}