package com.example.architecture.tasks

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.architecture.R
import com.example.architecture.data.Task
import com.example.architecture.data.TaskRepository
import com.example.architecture.util.Async
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * @项目 architecture
 * ＠包 com.example.architecture.tasks
 * @作者 bobo
 * @日期及日间 2024/11/26 18:33
 **/

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _savedFilterType =
        savedStateHandle.getStateFlow(TASKS_FILTER_SAVED_STATE_KEY, TasksFilterType.ALL_TASKS)
    private val _filterUiInfo = _savedFilterType.map { getFilterUiInfo(it) }.distinctUntilChanged()
    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val _isLoading = MutableStateFlow(false)
    private val _filteredTasksAsync =
        combine(taskRepository.getTasksStream(), _savedFilterType) { tasks, type ->
            filterTasks(tasks, type)
        }.map { Async.Success(it) }
            .catch<Async<List<Task>>> { emit(Async.Error(R.string.loading_tasks_error)) }



    private fun filterTasks(tasks: List<Task>, filterType: TasksFilterType): List<Task> {
        val tasksToShow = ArrayList<Task>()
        for (task in tasks) {
            when (filterType) {
                TasksFilterType.ALL_TASKS -> tasksToShow.add(task)
                TasksFilterType.ACTIVE_TASKS -> if (task.isActive) {
                    tasksToShow.add(task)
                }

                TasksFilterType.COMPLETED_TASKS -> if (task.isCompleted) {
                    tasksToShow.add(task)
                }
            }
        }
        return tasksToShow
    }

    private fun getFilterUiInfo(requestType: TasksFilterType): FilteringUiInfo =
        when (requestType) {
            TasksFilterType.ALL_TASKS -> {
                FilteringUiInfo(R.string.label_all, R.string.no_tasks_all, R.drawable.logo_no_fill)
            }

            TasksFilterType.ACTIVE_TASKS -> {
                FilteringUiInfo(
                    R.string.label_active,
                    R.string.no_tasks_active,
                    R.drawable.ic_check_circle_96dp
                )
            }

            TasksFilterType.COMPLETED_TASKS -> {
                FilteringUiInfo(
                    R.string.label_completed,
                    R.string.no_tasks_completed,
                    R.drawable.ic_verified_user_96dp
                )
            }
        }
}


const val TASKS_FILTER_SAVED_STATE_KEY = "TASKS_FILTER_SAVED_STATE_KEY"

data class FilteringUiInfo(
    val currentFilteringLabel: Int = R.string.label_all,
    val noTasksLabel: Int = R.string.no_tasks_all,
    val noTaskIconRes: Int = R.drawable.logo_no_fill
)