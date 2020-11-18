package com.example.samplegdc.feature.taskAdd

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
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
import com.example.samplegdc.job.NotificationJobService
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_task_new.*
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.temporal.ChronoUnit
import javax.inject.Inject

@AndroidEntryPoint
class TaskAddActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    @Inject
    lateinit var viewModel: TaskAddViewModel

    @Inject
    lateinit var repository: TaskRepository

    private var taskDate: TaskDateDto = TaskDateDto()

    private lateinit var taskDto: TaskDto

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
                var offsetDateTime: OffsetDateTime? = null
                if (taskDate.isDateReady() && taskDate.isTimeReady()) {
                    offsetDateTime = setTaskDateTime(taskDate)
                } else {
                    Toast.makeText(this, R.string.date_not_set, Toast.LENGTH_LONG).show()
                }
                taskDto = TaskDto(name = name, createdAt = offsetDateTime)
                viewModel.insert(taskDto)
                createAJobScheduler(taskDto)
                finish()
            } else {
                Snackbar.make(edtTaskNewName, R.string.name_required, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    /**Create a JobScheduler
     * [serviceName] is used to associate the JobService with the JobInfo object.
     * [timeTilFuture] is the time between the actual date and the date set for the task
     */
    private fun createAJobScheduler(taskDto: TaskDto) {
        val scheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val serviceName = ComponentName(packageName, NotificationJobService::class.java.name)

        val timeTilFuture = ChronoUnit.MILLIS.between(OffsetDateTime.now(), taskDto.createdAt)

        val jobBuilder = JobInfo.Builder(0, serviceName)
            .setMinimumLatency(timeTilFuture)

        val extras = PersistableBundle()
        extras.putString(SCHEDULE_EXTRA_TASK_NAME, taskDto.name)

        val jobInfo = jobBuilder.setExtras(extras).build()
        scheduler.schedule(jobInfo)
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
        taskDate = taskDate.copy(day = dayOfMonth, month = month + 1, year = year)
        val date = "$dayOfMonth/${this.taskDate.month}/$year"
        txtTaskNewDate.text = date
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        taskDate = taskDate.copy(minute = minute, hour = hourOfDay)
        val time = "$hourOfDay:$minute"
        txtTaskNewTime.text = time
    }


    companion object {

        const val SCHEDULE_EXTRA_TASK_NAME = "SCHEDULE_EXTRA_TASK_NAME"

        /**Start [TaskAddActivity]
         * @param context previous activity*/
        fun start(context: Context): Intent {
            return Intent(context, TaskAddActivity::class.java)
        }
    }
}