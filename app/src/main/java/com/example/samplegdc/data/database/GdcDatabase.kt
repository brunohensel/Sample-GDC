package com.example.samplegdc.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.samplegdc.data.entity.TaskDto
import com.example.samplegdc.data.local.TaskDao

@Database(entities = [TaskDto::class], version = 3, exportSchema = false)
abstract class GdcDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object {
        const val DATABASE_NAME: String = "tasks_db"
    }
}