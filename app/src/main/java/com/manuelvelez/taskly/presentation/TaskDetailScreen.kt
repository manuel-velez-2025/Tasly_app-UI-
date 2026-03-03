package com.manuelvelez.taskly.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.manuelvelez.taskly.domain.Task

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    task: Task,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (String) -> Unit,
    onDeleteTask: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("DETALLE TAREA") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = "Título: ${task.title}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(text = "Descripción: ${task.description}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Materia: ${task.subject}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Fecha: ${task.date}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Estado: ${if (task.isCompleted) "Completada" else "Pendiente"}", style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { onNavigateToEdit(task.id) }) {
                    Text("EDITAR")
                }
                Button(
                    onClick = {
                        onDeleteTask(task.id)
                        onNavigateBack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("ELIMINAR")
                }
            }
        }
    }
}