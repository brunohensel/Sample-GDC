package com.example.samplegdc.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.samplegdc.data.entity.TaskDto

/**[Dao] means data access object.
 * In this interface the SQL queries will be specified and the method calls will be associated
 * with those queries*/

@Dao
interface TaskDao {

    @Query("SELECT * FROM task_table ORDER BY name ASC")
    fun getAllTasks(): LiveData<List<TaskDto>>

    @Query("SELECT * FROM task_table WHERE id = :id")
    fun getTaskById(id: Int): LiveData<TaskDto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(taskDto: TaskDto)

    @Query("DELETE FROM task_table")
    fun deleteAll()

    @Query("DELETE FROM task_table WHERE id = :id")
    fun deleteById(id: Int)
}