package com.manuelvelez.taskly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.manuelvelez.taskly.data.RetrofitClient
import com.manuelvelez.taskly.data.TaskRepositoryImpl
import com.manuelvelez.taskly.presentation.*
import com.manuelvelez.taskly.ui.theme.TasklyTheme

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

                    val factory = TasksViewModelFactory(repository)
                    val viewModel: TasksViewModel = viewModel(factory = factory)

                    val state by viewModel.uiState.collectAsState()
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "list") {

                        composable("list") {
                            TaskListScreen(
                                viewModel = viewModel,
                                onNavigateToCreate = { navController.navigate("create") },
                                onNavigateToDetail = { taskId -> navController.navigate("detail/$taskId") }
                            )
                        }

                        composable("create") {
                            TaskCreateScreen(
                                viewModel = viewModel,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }

                        composable("detail/{taskId}") { backStackEntry ->
                            val taskId = backStackEntry.arguments?.getString("taskId")
                            val task = state.tasks.find { it.id == taskId }

                            if (task != null) {
                                TaskDetailScreen(
                                    task = task,
                                    onNavigateBack = { navController.popBackStack() },
                                    onNavigateToEdit = { id -> navController.navigate("edit/$id") },
                                    onDeleteTask = { id -> viewModel.deleteTask(id) }
                                )
                            }
                        }

                        composable("edit/{taskId}") { backStackEntry ->
                            val taskId = backStackEntry.arguments?.getString("taskId")
                            val task = state.tasks.find { it.id == taskId }

                            if (task != null) {
                                TaskEditScreen(
                                    task = task,
                                    onNavigateBack = { navController.popBackStack() },
                                    onUpdateTask = { updatedTask -> viewModel.updateTask(updatedTask) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}