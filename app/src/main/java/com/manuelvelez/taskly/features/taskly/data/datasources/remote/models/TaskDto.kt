package com.manuelvelez.taskly.features.taskly.data.datasources.remote.models

import com.google.gson.annotations.SerializedName

data class TaskDto(
    @SerializedName("id") val id: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("subject") val subject: String?,
    @SerializedName("date") val date: String?,
    @SerializedName("isCompleted") val isCompleted: Boolean?
)