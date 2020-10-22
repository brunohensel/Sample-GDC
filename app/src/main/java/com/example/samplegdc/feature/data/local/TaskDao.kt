package com.example.samplegdc.feature.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.samplegdc.feature.data.entity.TaskDto

/**[Dao] means data access object.
 * In this interface the SQL queries will be specified and the method calls will be associated
 * with those queries*/

@Dao
interface TaskDao {

    @Query("SELECT * FROM task_table ORDER BY name ASC")
    fun getAllTasks(): LiveData<List<TaskDto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(taskDto: TaskDto)
}