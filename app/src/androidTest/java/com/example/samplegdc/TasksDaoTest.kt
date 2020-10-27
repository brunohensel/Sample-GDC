package com.example.samplegdc

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.samplegdc.data.database.GdcDatabase
import com.example.samplegdc.data.entity.TaskDto
import com.example.samplegdc.data.local.TaskDao
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TasksDaoTest {

    private lateinit var taskDao: TaskDao
    private lateinit var db: GdcDatabase

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        /**Using an in-memory database because the information stored here disappears when the
         *process is killed.*/

        db = Room.inMemoryDatabaseBuilder(context, GdcDatabase::class.java)
            /**Allowing main thread queries, just for testing.*/
            .allowMainThreadQueries()
            .build()

        taskDao = db.taskDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun test() {
        println("test")
    }

    @Test
    fun getAllTasks() {
        //Given - nothing to be given

        //When
        val allTasks = taskDao.getAllTasks().waitForValue()

        //Then
        assertEquals(allTasks.size, 0)
    }

    @Test
    fun deleteAll() = runBlocking {
        //Given - nothing to be given
        val task = TaskDto(name = "test")
        taskDao.insert(task)
        //When
        taskDao.deleteAll()

        //Then
        val allTasks = taskDao.getAllTasks().waitForValue()
        assertEquals(allTasks.size, 0)
    }

    @Test
    fun getTaskById() = runBlocking {
        //Given
        val task = TaskDto(name = "test")
        taskDao.insert(task)
        val allTasks = taskDao.getAllTasks().waitForValue()
        val clickedTask = allTasks[0]

        //When
        val taskId: TaskDto = taskDao.getTaskById(clickedTask.id).waitForValue()

        //Then
        assertEquals(taskId.name, task.name)
    }

    @Test
    fun deleteById() = runBlocking {
        //Given
        val task1 = TaskDto(name = "test")
        val task2 = TaskDto(name = "test2")
        taskDao.insert(task1)
        taskDao.insert(task2)
        val allTasks = taskDao.getAllTasks().waitForValue()
        val deletedTask = allTasks[0]

        //When
        taskDao.deleteById(deletedTask.id)

        //Then
        val allTasksAfterDelete = taskDao.getAllTasks().waitForValue()
        assertEquals(deletedTask.name, task1.name)
        assertEquals(allTasksAfterDelete.size, 1)
    }

    @Test
    fun insert() = runBlocking {
        //Given
        val task = TaskDto(name = "test")

        //When
        taskDao.insert(task)

        //Then
        val allTasks = taskDao.getAllTasks().waitForValue()
        assertEquals(allTasks[0].name, task.name)
    }


    @Test
    fun edit() {
        //Given
        val task = TaskDto(name = "task")
        runBlocking {
            taskDao.insert(task)
        }
        val allTasks = taskDao.getAllTasks().waitForValue()
        val updatedTask = allTasks[0].copy(name = "NewName")

        //When
        taskDao.upDate(updatedTask)

        //Then
        val refreshedTask = taskDao.getAllTasks().waitForValue()
        assertEquals(refreshedTask[0].name, updatedTask.name)
    }
}