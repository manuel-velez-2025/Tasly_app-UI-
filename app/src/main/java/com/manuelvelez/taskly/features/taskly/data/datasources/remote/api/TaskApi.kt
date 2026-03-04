package com.manuelvelez.taskly.features.taskly.data.datasources.remote.api

import com.manuelvelez.taskly.features.taskly.data.datasources.remote.models.TaskDto
import retrofit2.Response
import retrofit2.http.*

interface TaskApi {
    @GET("/tasks")
    suspend fun getTasks(): List<TaskDto>
    @POST("/tasks")
    suspend fun createTask(@Body task: TaskDto): Response<Void>
    @PUT("/tasks/{id}")
    suspend fun updateTask(@Path("id") id: String, @Body task: TaskDto): Response<Void>
    @DELETE("/tasks/{id}")
    suspend fun deleteTask(@Path("id") id: String): Response<Void>
}