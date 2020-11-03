package com.example.samplegdc.feature.taskDetail

import android.app.Application
import androidx.lifecycle.*
import com.example.samplegdc.data.entity.TaskDto
import com.example.samplegdc.domain.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class TaskDetailViewModel @Inject constructor(
    application: Application,
    private val repository: TaskRepository
) : AndroidViewModel(application) {

    private val _id = MutableLiveData<Long>()

    private var _task: LiveData<TaskDto> = _id
        .switchMap { id ->
            repository.getTaskById(id)
        }

    val task: LiveData<TaskDto> = _task

    fun start(id: Long) {
        _id.value = id
    }

    fun update(taskDto: TaskDto) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(taskDto)
        }
    }

    fun delete(taskId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteById(taskId)
        }
    }


    class TaskViewModelFactory @Inject constructor(
        private val application: Application,
        private val repository: TaskRepository
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(TaskDetailViewModel::class.java)) {
                TaskDetailViewModel(
                    this.application, repository
                ) as T
            } else {
                throw IllegalArgumentException("ViewModel Not Found")
            }
        }
    }
}
