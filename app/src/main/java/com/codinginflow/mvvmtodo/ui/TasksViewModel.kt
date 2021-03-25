package com.codinginflow.mvvmtodo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.codinginflow.mvvmtodo.util.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val repository: TasksRepository,
    private val preferencesManager: PreferencesManager
): ViewModel() {

    val searchQuery = MutableStateFlow("")
    val preferencesFlow = preferencesManager.preferencesFlow

    private val tasksFlow = combine(searchQuery , preferencesFlow){ searchQuery ,filterPreferences ->
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

}
enum class SortOrder { BY_NAME, BY_DATE }