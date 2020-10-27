package com.example.samplegdc.taskAdd

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.samplegdc.application.GdcApplication
import com.example.samplegdc.data.entity.TaskDto
import com.example.samplegdc.domain.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class TaskAddViewModel @Inject constructor(
    application: Application,
    private val repository: TaskRepository
) : AndroidViewModel(application) {

    fun insert(taskDto: TaskDto) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTask(taskDto)
        }
    }

    class TasksViewModelFactory @Inject constructor(
        private val application: GdcApplication,
        private val repository: TaskRepository
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(TaskAddViewModel::class.java)) {
                TaskAddViewModel(application, repository) as T
            } else {
                throw  IllegalArgumentException("ViewModel not found")
            }
        }
    }
}