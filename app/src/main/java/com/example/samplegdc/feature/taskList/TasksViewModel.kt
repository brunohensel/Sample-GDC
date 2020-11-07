package com.example.samplegdc.feature.taskList

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

    private val _filter = MutableLiveData<FilterIntent>()
    private val _allTasks: LiveData<List<TaskDto>> = _filter
        .switchMap {
            when (it) {
                FilterIntent.ASC -> repository.getAllTasks()
                FilterIntent.DATE -> repository.orderByDateDESC()
                else -> {
                    throw java.lang.IllegalArgumentException("Unknown filter option $it")
                }
            }
        }

    init {
        filter(FilterIntent.ASC)
    }

    var allTasks: LiveData<List<TaskDto>> = _allTasks

    fun filter(intent: FilterIntent) {
        _filter.value = intent
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

enum class FilterIntent {
    ASC,
    DATE
}

