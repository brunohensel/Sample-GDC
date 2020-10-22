package com.example.samplegdc.feature.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**The [@Entity] class represents a SQLite table.*/
@Entity(tableName = "task_table")
data class TaskDto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val state: String
)

enum class Status {
    TODO,
    PROGRESS,
    DONE
}