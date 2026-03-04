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
                    val api = RetrofitClient.api
                    val repository = TaskRepositoryImpl(api)
                    val getTasksUseCase = GetTasksUseCase(repository)
                    val createTaskUseCase = CreateTaskUseCase(repository)
                    val updateTaskUseCase = UpdateTaskUseCase(repository)
                    val deleteTaskUseCase = DeleteTaskUseCase(repository)
                    val factory = TasksViewModelFactory(
                        getTasksUseCase,
                        createTaskUseCase,
                        updateTaskUseCase,
                        deleteTaskUseCase
                    )
                    val viewModel: TasksViewModel = viewModel(factory = factory)
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "list") {
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
                                            viewModel.selectTask(task)
                                            navController.navigate("detail")
                                        }
                                    )
                                }
                            }
                        }
                        composable("create") {
                            TaskCreateScreen(
                                viewModel = viewModel,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                        composable("detail") {
                            TaskDetailScreen(
                                viewModel = viewModel,
                                onNavigateToEdit = { navController.navigate("edit") },
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                        composable("edit") {
                            TaskEditScreen(
                                viewModel = viewModel,
                                onNavigateBack = {
                                    navController.popBackStack()
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
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