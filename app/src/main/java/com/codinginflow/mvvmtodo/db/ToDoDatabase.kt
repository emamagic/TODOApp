package com.codinginflow.mvvmtodo.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.codinginflow.mvvmtodo.db.entity.Task
import com.codinginflow.mvvmtodo.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Task::class] ,version = 1)
abstract class ToDoDatabase: RoomDatabase() {
    abstract fun getDao(): ToDoDao

    class Callback @Inject constructor(
        private val database: Provider<ToDoDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ): RoomDatabase.Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().getDao()

            applicationScope.launch {
                dao.insert(Task("Wash the dishes"))
                dao.insert(Task("Do the laundry"))
                dao.insert(Task("Buy groceries", important = true))
                dao.insert(Task("Prepare food", completed = true))
                dao.insert(Task("Call mom"))
                dao.insert(Task("Visit grandma", completed = true))
                dao.insert(Task("Repair my bike"))
                dao.insert(Task("Call Elon Musk"))
            }
        }
    }
}