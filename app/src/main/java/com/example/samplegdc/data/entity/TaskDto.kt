package com.example.samplegdc.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.OffsetDateTime

/**The [@Entity] class represents a SQLite table.*/
@Entity(tableName = "task_table")
data class TaskDto(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val state: String = Status.TODO.name,
    val createdAt: OffsetDateTime? = null
)

enum class Status {
    TODO,
    PROGRESS,
    DONE
}