package com.example.samplegdc.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.samplegdc.R
import com.example.samplegdc.application.GdcApplication
import com.example.samplegdc.data.entity.TaskDto
import com.example.samplegdc.domain.TaskRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: TasksViewModel

    @Inject
    lateinit var repository: TaskRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(
            this,
            TasksViewModel.TasksViewModelFactory(GdcApplication.instance, repository)
        ).get(TasksViewModel::class.java)

        viewModel.allTasks.observe(this) {
            Log.i("MyTaskList", it.toString())
        }

        viewModel.addTask(TaskDto(name = "test"))
    }
}