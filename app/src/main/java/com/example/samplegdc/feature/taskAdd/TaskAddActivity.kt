package com.example.samplegdc.feature.taskAdd

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.samplegdc.R
import com.example.samplegdc.application.GdcApplication
import com.example.samplegdc.data.entity.TaskDateDto
import com.example.samplegdc.data.entity.TaskDto
import com.example.samplegdc.domain.TaskRepository
import com.example.samplegdc.feature.DatePickerFragment
import com.example.samplegdc.feature.TimePickerFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_task_new.*
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject

@AndroidEntryPoint
class TaskAddActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    @Inject
    lateinit var viewModel: TaskAddViewModel

    @Inject
    lateinit var repository: TaskRepository

    private var taskDate: TaskDateDto = TaskDateDto()
    private lateinit var offsetDateTime: OffsetDateTime

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_new)

        //Add arrow back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(
            this,
            TaskAddViewModel.TasksViewModelFactory(GdcApplication.instance, repository)
        ).get(TaskAddViewModel::class.java)

        txtTaskNewDate.setOnClickListener {
            showDatePickerDialog()
        }

        txtTaskNewTime.setOnClickListener {
            showTimePickerDialog()
        }

        btnTaskNewSave.setOnClickListener {
            val name = edtTaskNewName.text.toString()
            if (name.isNotEmpty()) {
                if (taskDate.isDateReady() && taskDate.isTimeReady()) {
                    offsetDateTime = setTaskDateTime(taskDate)
                } else {
                    Snackbar.make(edtTaskNewName, R.string.date_not_set, Snackbar.LENGTH_LONG)
                        .show()
                }
                viewModel.insert(TaskDto(name = name, createdAt = offsetDateTime))
                finish()
            } else {
                Snackbar.make(edtTaskNewName, R.string.name_required, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun showTimePickerDialog() {
        val newFragment: DialogFragment = TimePickerFragment.newInstance(this)
        newFragment.show(supportFragmentManager, "timePicker")
    }

    private fun showDatePickerDialog() {
        val newFragment: DialogFragment = DatePickerFragment.newInstance(this)
        newFragment.show(supportFragmentManager, "datePicker")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            else -> return super.onOptionsItemSelected(item)
        }
        return false
    }

    companion object {
        /**Start [TaskAddActivity]
         * @param context previous activity*/
        fun start(context: Context): Intent {
            return Intent(context, TaskAddActivity::class.java)
        }
    }

    private fun setTaskDateTime(taskDateDto: TaskDateDto): OffsetDateTime {
        return OffsetDateTime.of(
            taskDateDto.year,
            taskDateDto.month,
            taskDateDto.day,
            taskDateDto.hour,
            taskDateDto.minute,
            0,
            0,
            OffsetDateTime.now().offset
        )
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        taskDate = taskDate.copy(day = dayOfMonth, month = month, year = year)
        val date = "$dayOfMonth/${this.taskDate.month}/$year"
        txtTaskNewDate.text = date
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        taskDate = taskDate.copy(minute = minute, hour = hourOfDay)
        val time = "$hourOfDay:$minute"
        txtTaskNewTime.text = time
    }
}