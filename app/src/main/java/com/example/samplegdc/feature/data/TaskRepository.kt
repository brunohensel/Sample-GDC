package com.example.samplegdc.feature.data

import androidx.lifecycle.LiveData
import com.example.samplegdc.feature.data.entity.TaskDto
import com.example.samplegdc.feature.data.local.TaskDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(private val taskDao: TaskDao) {

    suspend fun addTask(taskDto: TaskDto) {
        taskDao.insert(taskDto)
    }

    fun getAllTasks(): LiveData<List<TaskDto>> = taskDao.getAllTasks()
}