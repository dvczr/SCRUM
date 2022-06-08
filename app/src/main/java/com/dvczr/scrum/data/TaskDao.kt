package com.dvczr.scrum.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dvczr.scrum.model.Task

@Dao
interface TaskDao  {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(task: Task)

    @Update
    suspend fun editTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("DELETE FROM task_table")
    suspend fun deleteAllTasks()

    @Query("SELECT * FROM task_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE title LIKE :searchQuery OR body LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): LiveData<List<Task>>
}