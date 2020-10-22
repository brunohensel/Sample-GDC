package com.example.samplegdc.feature.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.samplegdc.feature.data.entity.TaskDto
import com.example.samplegdc.feature.data.local.TaskDao

@Database(entities = [TaskDto::class], version = 1, exportSchema = false)
abstract class GdcDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object {
        const val DATABASE_NAME: String = "tasks_db"
    }
}