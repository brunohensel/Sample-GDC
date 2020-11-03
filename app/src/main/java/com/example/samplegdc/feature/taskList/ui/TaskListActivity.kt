package com.example.samplegdc.feature.taskList.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.samplegdc.R
import com.example.samplegdc.application.GdcApplication
import com.example.samplegdc.data.entity.TaskDto
import com.example.samplegdc.domain.TaskRepository
import com.example.samplegdc.feature.taskAdd.TaskAddActivity
import com.example.samplegdc.feature.taskDetail.TaskDetailActivity
import com.example.samplegdc.feature.taskList.TasksViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_task_list.*
import javax.inject.Inject

@AndroidEntryPoint
class TaskListActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: TasksViewModel

    @Inject
    lateinit var repository: TaskRepository

    private lateinit var tasksAdapter: TaskListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        tasksAdapter = TaskListAdapter(this, ::tasksClickListener)
        rvTaskList.adapter = tasksAdapter

        viewModel = ViewModelProvider(
            this,
            TasksViewModel.TasksViewModelFactory(GdcApplication.instance, repository)
        ).get(TasksViewModel::class.java)

        initObservers()

        fabAddTask.setOnClickListener {
            val intent = TaskAddActivity.start(this)
            startActivity(intent)
        }

        viewModel.addTask(TaskDto(name = "test"))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_task_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete_all -> viewModel.deleteAll()
            else -> return super.onOptionsItemSelected(item)
        }
        return false
    }

    private fun initObservers() {
        viewModel.allTasks.observe(this) {
            tasksAdapter.submit(it)
        }
    }

    private fun tasksClickListener(id: Long) {
        startActivity(TaskDetailActivity.start(this, id))
    }
}