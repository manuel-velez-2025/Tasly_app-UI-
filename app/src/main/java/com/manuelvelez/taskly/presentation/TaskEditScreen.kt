package com.manuelvelez.taskly.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.manuelvelez.taskly.domain.Task

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEditScreen(
    task: Task,
    onNavigateBack: () -> Unit,
    onUpdateTask: (Task) -> Unit
) {
    var title by remember { mutableStateOf(task.title) }
    var description by remember { mutableStateOf(task.description) }
    var subject by remember { mutableStateOf(task.subject) }
    var date by remember { mutableStateOf(task.date) }
    var isCompleted by remember { mutableStateOf(task.isCompleted) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("EDITAR TAREA") },
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
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            OutlinedTextField(
                value = subject,
                onValueChange = { subject = it },
                label = { Text("Materia") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = date,
                onValueChange = { date = it },
                label = { Text("Fecha") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Estado:", style = MaterialTheme.typography.bodyLarge)

            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = !isCompleted,
                    onClick = { isCompleted = false }
                )
                Text("Pendiente")

                Spacer(modifier = Modifier.width(16.dp))

                RadioButton(
                    selected = isCompleted,
                    onClick = { isCompleted = true }
                )
                Text("Completada")
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val updatedTask = task.copy(
                        title = title,
                        description = description,
                        subject = subject,
                        date = date,
                        isCompleted = isCompleted
                    )
                    onUpdateTask(updatedTask)
                    onNavigateBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("ACTUALIZAR")
            }
        }
    }
}