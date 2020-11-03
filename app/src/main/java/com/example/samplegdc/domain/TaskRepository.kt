package com.example.samplegdc.domain

import androidx.lifecycle.LiveData
import com.example.samplegdc.data.entity.TaskDto
import com.example.samplegdc.data.local.TaskDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(private val taskDao: TaskDao) {

    suspend fun addTask(taskDto: TaskDto) {
        taskDao.insert(taskDto)
    }

    fun getAllTasks(): LiveData<List<TaskDto>> = taskDao.getAllTasks()

    fun getTaskById(taskId: Long): LiveData<TaskDto> = taskDao.getTaskById(taskId)

    suspend fun update(taskDto: TaskDto) {
        taskDao.upDate(taskDto)
    }

    fun deleteById(taskId: Long) {
        taskDao.deleteById(taskId)
    }

    suspend fun deleteAll() {
        taskDao.deleteAll()
    }
}