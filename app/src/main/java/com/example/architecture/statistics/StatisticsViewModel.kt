package com.example.architecture.statistics

import androidx.compose.runtime.produceState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.architecture.R
import com.example.architecture.data.Task
import com.example.architecture.data.TaskRepository
import com.example.architecture.util.Async
import com.example.architecture.util.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @项目 architecture
 * ＠包 com.example.architecture.statistics
 * @作者 bobo
 * @日期及日间 2024/11/28 20:35
 **/

data class StatisticsUiState(
    val isEmpty: Boolean = false,
    val isLoading: Boolean = false,
    val activeTasksPercent: Float = 0f,
    val completedTasksPercent: Float = 0f
)


@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {
    val uiState: StateFlow<StatisticsUiState> =
        taskRepository.getTasksStream().map { Async.Success(it) }.catch<Async<List<Task>>> {
            emit(
                Async.Error(
                    R.string.loading_tasks_error
                )
            )
        }.map { taskAsync -> produceStatisticsUiState(taskAsync) }.stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = StatisticsUiState(isLoading = true)
        )

    fun refresh() {
        viewModelScope.launch {
            taskRepository.refresh()
        }
    }


    private fun produceStatisticsUiState(taskLoad: Async<List<Task>>) = when (taskLoad) {
        Async.Loading -> {
            StatisticsUiState(isLoading = true, isEmpty = true)
        }
        // 显示错误信息。
        is Async.Error -> {
            StatisticsUiState(isEmpty = true, isLoading = false)
        }

        is Async.Success -> {
            val stats = getActiveAndCompletedStats(taskLoad.data)
            StatisticsUiState(
                isEmpty = taskLoad.data.isEmpty(),
                activeTasksPercent = stats.activeTasksPercent,
                completedTasksPercent = stats.completedTasksPercent,
                isLoading = false
            )
        }
    }
}