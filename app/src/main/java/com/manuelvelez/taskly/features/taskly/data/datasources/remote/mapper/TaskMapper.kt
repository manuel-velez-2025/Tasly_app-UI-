package com.manuelvelez.taskly.features.taskly.data.datasources.remote.mapper

import com.manuelvelez.taskly.features.taskly.data.datasources.remote.models.TaskDto
import com.manuelvelez.taskly.features.taskly.domain.entities.Task

fun TaskDto.toDomain(): Task {
    return Task(
        id = this.id ?: "",
        title = this.title ?: "Sin título",
        description = this.description ?: "",
        subject = this.subject ?: "General",
        date = this.date ?: "",
        isCompleted = this.isCompleted ?: false
    )
}
fun Task.toDto(): TaskDto {
    return TaskDto(
        id = this.id,
        title = this.title,
        description = this.description,
        subject = this.subject,
        date = this.date,
        isCompleted = this.isCompleted
    )
}