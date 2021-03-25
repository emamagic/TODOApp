package com.codinginflow.mvvmtodo.ui

import com.codinginflow.mvvmtodo.db.entity.Task
import com.codinginflow.mvvmtodo.safe.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface TasksRepository {

    suspend fun insertTask(task: Task)

    suspend fun updateTask(task: Task)

    suspend fun deleteTask(task: Task)

    fun getTasks(searchQuery: String, sortOrder: SortOrder, hideCompleted: Boolean): Flow<ResultWrapper<List<Task>>>
}