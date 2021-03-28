package com.codinginflow.mvvmtodo.di

import com.codinginflow.mvvmtodo.ui.task.TasksRepository
import com.codinginflow.mvvmtodo.ui.task.TasksRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class BinderModule {

    @Binds
    abstract fun bindTaskRepository(tasksRepositoryImpl: TasksRepositoryImpl): TasksRepository

}