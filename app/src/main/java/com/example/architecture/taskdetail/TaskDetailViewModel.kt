package com.example.architecture.taskdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.architecture.R
import com.example.architecture.TodoDestinationsArgs
import com.example.architecture.data.Task
import com.example.architecture.data.TaskRepository
import com.example.architecture.util.Async
import com.example.architecture.util.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @项目 architecture
 * ＠包 com.example.architecture.taskdetail
 * @作者 bobo
 * @日期及日间 2024/12/3 09:27
 **/

data class TaskDetailUiState(
    val task: Task? = null,
    val isLoading: Boolean = false,
    val userMessage: Int? = null,
    val isTaskDeleted: Boolean = false
)


@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val taskId: String = savedStateHandle[TodoDestinationsArgs.TASK_ID_ARG]!!
    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val _isLoading = MutableStateFlow(false)
    private val _isTaskDeleted = MutableStateFlow(false)
    private val _taskAsync = taskRepository.getTaskStream(taskId).map { handleTask(it) }.catch {
        emit(
            Async.Error(
                R.string.loading_tasks_error
            )
        )
    }
    val uiState: StateFlow<TaskDetailUiState> =
        combine(
            _userMessage,
            _isLoading,
            _isTaskDeleted,
            _taskAsync
        ) { userMessage, isLoading, isTaskDeleted, taskAsync ->
            when (taskAsync) {
                Async.Loading -> {
                    TaskDetailUiState(isLoading = true)
                }

                is Async.Error -> {
                    TaskDetailUiState(
                        userMessage = taskAsync.errorMessage,
                        isTaskDeleted = isTaskDeleted
                    )
                }

                is Async.Success -> {
                    TaskDetailUiState(
                        task = taskAsync.data,
                        isLoading = isLoading,
                        userMessage = userMessage,
                        isTaskDeleted = isTaskDeleted
                    )
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = TaskDetailUiState(isLoading = true)
        )

    fun deleteTask() = viewModelScope.launch {
        taskRepository.deleteTask(taskId)
        _isTaskDeleted.value = true
    }

    fun setCompleted(completed: Boolean) = viewModelScope.launch {
        val task = uiState.value.task ?: return@launch
        if (completed) {
            taskRepository.completeTask(task.id)
            showSnackbarMessage(R.string.task_marked_complete)
        } else {
            taskRepository.activateTask(task.id)
            showSnackbarMessage(R.string.task_marked_active)
        }
    }

    fun refresh() {
        _isLoading.value = true
        viewModelScope.launch {
            taskRepository.refreshTask(taskId)
            _isLoading.value = false
        }
    }

    fun snackbarMessageShown() {
        _userMessage.value = null
    }

    private fun showSnackbarMessage(message: Int) {
        _userMessage.value = message
    }

    private fun handleTask(task: Task?): Async<Task?> {
        if (task == null) {
            return Async.Error(R.string.task_not_found)
        }
        return Async.Success(task)
    }
}
