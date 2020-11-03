package com.example.samplegdc.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.samplegdc.data.entity.TaskDto
import com.example.samplegdc.data.local.TaskDao

@Database(entities = [TaskDto::class], version = 4, exportSchema = false)
@TypeConverters(DateTypeConverters::class)
abstract class GdcDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object {
        const val DATABASE_NAME: String = "tasks_db"
    }
}