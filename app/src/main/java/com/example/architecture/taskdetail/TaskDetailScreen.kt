package com.example.architecture.taskdetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.architecture.R
import com.example.architecture.data.Task
import com.example.architecture.ui.theme.ArchitectureTheme
import com.example.architecture.util.LoadingContent
import com.example.architecture.util.TaskDetailTopAppBar

/**
 * @项目 architecture
 * ＠包 com.example.architecture.taskdetail
 * @作者 bobo
 * @日期及日间 2024/12/3 09:24
 **/

@Composable
fun TaskDetailScreen(
    modifier: Modifier = Modifier,
    onEditTask: (String) -> Unit,
    onBack: () -> Unit,
    onDeleteTask: () -> Unit,
    viewModel: TaskDetailViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier.fillMaxSize(),
        topBar = {
            TaskDetailTopAppBar(onBack = onBack, onDelete = viewModel::deleteTask)
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onEditTask(viewModel.taskId) }) {
                Icon(Icons.Filled.Edit, stringResource(id = R.string.edit_task))
            }
        }
    ) { paddingValues ->
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        EditTaskContent(
            loading = uiState.isLoading,
            empty = uiState.task == null && !uiState.isLoading,
            task = uiState.task,
            onRefresh = viewModel::refresh,
            onTaskCheck = viewModel::setCompleted,
            modifier = Modifier.padding(paddingValues)
        )

        uiState.userMessage?.let { userMessage ->
            val snackbarText = stringResource(userMessage)
            LaunchedEffect(snackbarHostState,viewModel,userMessage,snackbarText) {
                snackbarHostState.showSnackbar(snackbarText)
                viewModel.snackbarMessageShown()
            }
        }
        LaunchedEffect(uiState.isTaskDeleted) {
            if (uiState.isTaskDeleted){
                onDeleteTask()
            }
        }
    }
}

@Composable
private fun EditTaskContent(
    modifier: Modifier = Modifier,
    loading: Boolean,
    empty: Boolean,
    task: Task?,
    onTaskCheck: (Boolean) -> Unit,
    onRefresh: () -> Unit,
) {
    val screenPadding = Modifier.padding(
        horizontal = dimensionResource(id = R.dimen.horizontal_margin),
        vertical = dimensionResource(id = R.dimen.vertical_margin)
    )
    val commonModifier = modifier
        .fillMaxWidth()
        .then(screenPadding)

    LoadingContent(
        loading = loading,
        empty = empty,
        emptyContent = {
            Text(
                text = stringResource(id = R.string.no_data),
                modifier = commonModifier
            )
        },
        onRefresh = onRefresh
    ) {
        Column(
            commonModifier.verticalScroll(rememberScrollState())
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .then(screenPadding)
            ) {
                if (task != null) {
                    Checkbox(task.isCompleted, onTaskCheck)
                    Column {
                        Text(text = task.title, style = MaterialTheme.typography.titleMedium)
                        Text(text = task.description, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun EditTaskContentPreview() {
    ArchitectureTheme {
        Surface {
            EditTaskContent(
                modifier = Modifier.fillMaxWidth(),
                loading = false,
                empty = false,
                Task(
                    title = "Title",
                    description = "Description",
                    isCompleted = true,
                    id = "ID"
                ),
                onTaskCheck = { },
                onRefresh = { }
            )
        }
    }
}

@Preview
@Composable
private fun EditTaskContentTaskCompletedPreview() {
    ArchitectureTheme {
        Surface {
            EditTaskContent(
                modifier = Modifier.fillMaxWidth(),
                loading = false,
                empty = false,
                Task(
                    title = "Title",
                    description = "Description",
                    isCompleted = false,
                    id = "ID"
                ),
                onTaskCheck = { },
                onRefresh = { }
            )
        }
    }
}

@Preview
@Composable
private fun EditTaskContentEmptyPreview() {
    ArchitectureTheme {
        Surface {
            EditTaskContent(
                modifier = Modifier.fillMaxWidth(),
                loading = false,
                empty = true,
                Task(
                    title = "Title",
                    description = "Description",
                    isCompleted = false,
                    id = "ID"
                ),
                onTaskCheck = { },
                onRefresh = { }
            )
        }
    }
}
