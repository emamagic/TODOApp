package com.codinginflow.mvvmtodo.ui.task

import androidx.lifecycle.*
import com.codinginflow.mvvmtodo.db.entity.Task
import com.codinginflow.mvvmtodo.util.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
        private val repository: TasksRepository,
        private val preferencesManager: PreferencesManager,
        private val state: SavedStateHandle
): ViewModel() {

    val searchQuery = state.getLiveData("searchQuery" ,"")
    val preferencesFlow = preferencesManager.preferencesFlow
    private val tasksEventChannel = Channel<TasksEvent>()
    val tasksEvent = tasksEventChannel.receiveAsFlow()

    private val tasksFlow = combine(searchQuery.asFlow() , preferencesFlow){ searchQuery ,filterPreferences ->
        Pair(searchQuery ,filterPreferences)
    }.flatMapLatest { (searchQuery ,filterPreferences) ->
        repository.getTasks(searchQuery ,filterPreferences.sortOrder ,filterPreferences.hideCompleted)
    }

    val tasks = tasksFlow.asLiveData()

    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        preferencesManager.updateSortOrder(sortOrder)
    }

    fun onHildeCompletedClick(hideCompleted: Boolean) = viewModelScope.launch {
        preferencesManager.updateHideCompleted(hideCompleted)
    }

    fun onTaskSelected(task: Task) = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.NavigateToEditTaskScreen(task))
    }

    fun onTaskCheckedChanged(task: Task ,isChecked: Boolean) = viewModelScope.launch{
        repository.updateTask(task.copy(completed = isChecked))
    }

    fun onTaskSwiped(task: Task) = viewModelScope.launch {
        repository.deleteTask(task)
        tasksEventChannel.send(TasksEvent.ShowUndoDeleteTaskMessage(task))
    }

    sealed class TasksEvent {
        data class ShowUndoDeleteTaskMessage(val task: Task): TasksEvent()
        data class NavigateToEditTaskScreen(val task: Task): TasksEvent()
        object NavigateToAddTaskScreen: TasksEvent()
    }

    fun onUndoDeleteClick(task: Task) = viewModelScope.launch {
        repository.insertTask(task)
    }

    fun onAddNewTaskClick() = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.NavigateToAddTaskScreen)
    }

}
enum class SortOrder { BY_NAME, BY_DATE }