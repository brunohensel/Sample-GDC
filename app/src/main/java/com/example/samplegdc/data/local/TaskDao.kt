package com.example.samplegdc.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.samplegdc.data.entity.TaskDto

/**[Dao] means data access object.
 * In this interface the SQL queries will be specified and the method calls will be associated
 * with those queries*/

@Dao
interface TaskDao {

    @Query("SELECT * FROM task_table ORDER BY name ASC")
    fun getAllTasks(): LiveData<List<TaskDto>>

    @Query("SELECT * FROM task_table WHERE id = :id")
    fun getTaskById(id: Long): LiveData<TaskDto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(taskDto: TaskDto)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upDate(taskDto: TaskDto)

    @Query("DELETE FROM task_table")
    suspend fun deleteAll()

    @Query("DELETE FROM task_table WHERE id = :id")
    fun deleteById(id: Long)

    @Query(
        """
        SELECT * FROM task_table
        ORDER BY createdAt DESC
        """
    )
    fun orderByDateASC(): List<TaskDto>
}