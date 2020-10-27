package com.example.samplegdc.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**The [@Entity] class represents a SQLite table.*/
@Entity(tableName = "task_table")
data class TaskDto(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val state: String = Status.TODO.name
)

enum class Status {
    TODO,
    PROGRESS,
    DONE
}