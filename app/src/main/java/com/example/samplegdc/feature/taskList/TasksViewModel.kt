package com.example.samplegdc.feature.taskList

import android.app.Application
import androidx.lifecycle.*
import com.example.samplegdc.application.GdcApplication
import com.example.samplegdc.data.entity.TaskDto
import com.example.samplegdc.domain.TaskRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TasksViewModel @Inject constructor(
    application: Application,
    private val repository: TaskRepository
) : AndroidViewModel(application) {

    private val _allTasks: MutableLiveData<List<TaskDto>> = MutableLiveData()

    var allTasks: LiveData<List<TaskDto>> = _allTasks

    fun getAllTasks(lifecycleOwner: LifecycleOwner) {
        viewModelScope.launch(IO) {
            val tasks = repository.getAllTasks()
            withContext(Main) {
                tasks.observe(lifecycleOwner, Observer {
                    _allTasks.value = it
                })
            }
        }
    }

    fun orderByDateASC() {
        viewModelScope.launch(IO) {
            val tasks = repository.orderByDateASC()
            withContext(Main) {
                _allTasks.value = tasks
            }
        }
    }

    fun deleteAll() {
        viewModelScope.launch(IO) {
            repository.deleteAll()
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

