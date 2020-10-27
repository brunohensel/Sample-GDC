package com.example.samplegdc.taskList

import android.app.Application
import androidx.lifecycle.*
import com.example.samplegdc.application.GdcApplication
import com.example.samplegdc.data.entity.TaskDto
import com.example.samplegdc.domain.TaskRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class TasksViewModel @Inject constructor(
    application: Application,
    private val repository: TaskRepository
) : AndroidViewModel(application) {

    val allTasks: LiveData<List<TaskDto>>

    init {
        allTasks = repository.getAllTasks()
    }

    fun addTask(taskDto: TaskDto) {
        viewModelScope.launch(IO) {
            repository.addTask(taskDto)
        }
    }

    class TasksViewModelFactory @Inject constructor(
        private val application: GdcApplication,
        private val repository: TaskRepository
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(TasksViewModel::class.java)) {
                TasksViewModel(application, repository) as T
            } else {
                throw  IllegalArgumentException("ViewModel not found")
            }
        }
    }
}

