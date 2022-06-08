package com.dvczr.scrum.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.dvczr.scrum.data.TaskDao
import com.dvczr.scrum.model.Task

class TaskRepository(private val taskDao: TaskDao) {

    val readAllData: LiveData<List<Task>> = taskDao.readAllData()

    suspend fun addTask(task: Task) {
        taskDao.addUser(task)
    }

    suspend fun editTask(task: Task) {
        taskDao.editTask(task)
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

    suspend fun deleteAllTasks() {
        taskDao.deleteAllTasks()
    }

    @WorkerThread
    fun searchDatabase(searchQuery: String): LiveData<List<Task>> {
        return taskDao.searchDatabase(searchQuery)
    }
}