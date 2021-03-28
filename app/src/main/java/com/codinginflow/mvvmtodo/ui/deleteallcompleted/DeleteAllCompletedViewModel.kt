package com.codinginflow.mvvmtodo.ui.deleteallcompleted

import androidx.lifecycle.ViewModel
import com.codinginflow.mvvmtodo.di.ApplicationScope
import com.codinginflow.mvvmtodo.ui.task.TasksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeleteAllCompletedViewModel @Inject constructor(
        private val repository: TasksRepository,
        @ApplicationScope private val applicationScope: CoroutineScope
) : ViewModel() {

    fun onConfirmClick() = applicationScope.launch {
        repository.deleteCompletedTasks()
    }
}