package com.codinginflow.mvvmtodo.ui.addedittask

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.codinginflow.mvvmtodo.db.entity.Task
import com.codinginflow.mvvmtodo.ui.task.TasksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddEditTaskViewModel @Inject constructor(
        private val state: SavedStateHandle,
        private val repository: TasksRepository
) : ViewModel() {

    // get argument that safe arg pass it to fragment
    val task = state.get<Task>("task")

    var taskName = state.get<String>("taskName") ?: task?.name ?: ""
        set(value) {
            field = value
            state.set("taskName" ,value)
        }

    var taskImportance = state.get<Boolean>("taskImportance") ?: task?.important ?: false
        set(value) {
            field = value
            state.set("taskImportance" ,value)
        }

}