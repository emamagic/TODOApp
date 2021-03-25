package com.codinginflow.mvvmtodo.di

import android.content.Context
import androidx.room.Room
import com.codinginflow.mvvmtodo.db.ToDoDao
import com.codinginflow.mvvmtodo.db.ToDoDatabase
import com.codinginflow.mvvmtodo.util.Constants.DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {


    @Singleton
    @Provides
    fun provideToDoDatabase(@ApplicationContext context: Context ,callback: ToDoDatabase.Callback): ToDoDatabase {
        return Room.databaseBuilder(context , ToDoDatabase::class.java ,DB_NAME)
            .fallbackToDestructiveMigration()
            .addCallback(callback)
            .build()
    }

    @Provides
    fun provideToDoDao(toDoDatabase: ToDoDatabase): ToDoDao {
        return toDoDatabase.getDao()
    }


}