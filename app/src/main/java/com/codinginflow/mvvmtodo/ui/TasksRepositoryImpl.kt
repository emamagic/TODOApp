package com.codinginflow.mvvmtodo.ui

import com.codinginflow.mvvmtodo.db.ToDoDao
import com.codinginflow.mvvmtodo.db.entity.Task
import com.codinginflow.mvvmtodo.safe.GeneralErrorHandlerImpl
import com.codinginflow.mvvmtodo.safe.ResultWrapper
import com.codinginflow.mvvmtodo.safe.toResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TasksRepositoryImpl @Inject constructor(
    private val toDoDao: ToDoDao,
    private val errorHandler: GeneralErrorHandlerImpl
): TasksRepository {

    override suspend fun insertTask(task: Task) {
        toDoDao.insert(task)
    }

    override suspend fun updateTask(task: Task) {
        toDoDao.update(task)
    }

    override suspend fun deleteTask(task: Task) {
        toDoDao.delete(task)
    }

    override fun getTasks(searchQuery: String,sortOrder: SortOrder ,hideCompleted: Boolean): Flow<ResultWrapper<List<Task>>> {
        return toDoDao.getTasks(searchQuery ,sortOrder, hideCompleted).toResult(errorHandler)
    }


}