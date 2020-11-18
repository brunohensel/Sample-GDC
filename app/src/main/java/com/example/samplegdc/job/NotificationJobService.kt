package com.example.samplegdc.job

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import com.example.samplegdc.feature.taskAdd.TaskAddActivity

/**
 * The Service that JobScheduler runs once the conditions are met.
 * In this case it posts a notification.
 */
class NotificationJobService : JobService() {
    /**
     * Called by the system once it determines it is time to run the job.
     *
     * @param jobParameters Contains the information about the job.
     * @return Boolean indicating whether or not the job was offloaded to a
     * separate thread.
     * In this case, it is false since the notification can be posted on
     * the main thread.
     */
    override fun onStartJob(jobParameters: JobParameters): Boolean {
        val taskName = jobParameters.extras.getString(TaskAddActivity.SCHEDULE_EXTRA_TASK_NAME)
        Log.i(
            NotificationJobService::class.java.name,
            taskName ?: throw  IllegalArgumentException("no name")
        )
        return false
    }

    /**
     * Called by the system when the job is running but the conditions are no
     * longer met.
     * In this example it is never called since the job is not offloaded to a
     * different thread.
     *
     * @param jobParameters Contains the information about the job.
     * @return Boolean indicating whether the job needs rescheduling.
     */
    override fun onStopJob(jobParameters: JobParameters): Boolean {
        return false
    }
}