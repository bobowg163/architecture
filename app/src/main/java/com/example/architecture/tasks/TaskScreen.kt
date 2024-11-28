package com.example.architecture.tasks

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.architecture.R
import com.example.architecture.data.Task
import com.example.architecture.ui.theme.ArchitectureTheme
import com.example.architecture.util.LoadingContent
import com.example.architecture.util.TasksTopAppBar

/**
 * @项目 architecture
 * ＠包 com.example.architecture.tasks
 * @作者 bobo
 * @日期及日间 2024/11/25 19:55
 **/


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    modifier: Modifier = Modifier,
    @StringRes userMessage: Int,
    onAddTask: () -> Unit,
    onTaskClick: (Task) -> Unit,
    onUserMessageDisplayed: () -> Unit,
    openDrawer: () -> Unit,
    viewModel: TasksViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TasksTopAppBar(
                openDrawer = openDrawer,
                onFilterAllTasks = { viewModel.setFiltering(TasksFilterType.ALL_TASKS) },
                onFilterActiveTasks = { viewModel.setFiltering(TasksFilterType.ACTIVE_TASKS) },
                onFilterCompletedTasks = { viewModel.setFiltering(TasksFilterType.COMPLETED_TASKS) },
                onClearCompletedTasks = { viewModel.clearCompletedTasks() },
                onRefresh = { viewModel.refresh() }
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTask) {
                Icon(Icons.Filled.Add, stringResource(id = R.string.add_task))
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        TasksContent(
            loading = uiState.isLoading,
            tasks = uiState.items,
            currentFilteringLabel = uiState.filteringUiInfo.currentFilteringLabel,
            noTasksLabel = uiState.filteringUiInfo.noTasksLabel,
            noTasksIconRes = uiState.filteringUiInfo.noTaskIconRes,
            onRefresh = viewModel::refresh,
            onTaskClick = onTaskClick,
            onTaskCheckedChange = viewModel::completeTask,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun TasksContent(
    modifier: Modifier = Modifier,
    loading: Boolean,
    tasks: List<Task>,
    @StringRes currentFilteringLabel: Int,
    @StringRes noTasksLabel: Int,
    @DrawableRes noTasksIconRes: Int,
    onRefresh: () -> Unit,
    onTaskClick: (Task) -> Unit,
    onTaskCheckedChange: (Task, Boolean) -> Unit
) {
    LoadingContent(
        loading = loading,
        empty = tasks.isEmpty() && !loading,
        emptyContent = { TasksEmptyContent(noTasksLabel, noTasksIconRes, modifier) },
        onRefresh = onRefresh
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(id = R.dimen.horizontal_margin))
        ) {
            Text(
                text = stringResource(currentFilteringLabel),
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.list_item_padding),
                    vertical = dimensionResource(id = R.dimen.vertical_margin)
                ),
                style = MaterialTheme.typography.labelLarge
            )
            LazyColumn {
                items(tasks) { task ->
                    TaskItem(
                        task = task,
                        onTaskClick = onTaskClick,
                        onTaskCheckedChange = { onTaskCheckedChange(task, it) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TasksEmptyContent(
    @StringRes noTasksLabel: Int,
    @DrawableRes noTasksIconRes: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = noTasksIconRes),
            contentDescription = stringResource(R.string.no_tasks_image_content_description),
            modifier = Modifier.size(96.dp)
        )
        Text(stringResource(id = noTasksLabel))
    }
}

@Composable
private fun TaskItem(
    task: Task,
    onTaskCheckedChange: (Boolean) -> Unit,
    onTaskClick: (Task) -> Unit,
    modifier: Modifier = Modifier,

    ) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(id = R.dimen.horizontal_margin),
                vertical = dimensionResource(id = R.dimen.list_item_padding)
            )
            .clickable { onTaskClick(task) }
    ) {
        Checkbox(checked = task.isCompleted, onCheckedChange = onTaskCheckedChange)
        Text(
            text = task.titleForList,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.horizontal_margin)),
            textDecoration = if (task.isCompleted) {
                TextDecoration.LineThrough
            } else {
                null
            }
        )
    }
}

@Preview
@Composable
private fun TasksContentPreview() {
    ArchitectureTheme {
        Surface {
            TasksContent(
                loading = false,
                tasks = listOf(
                    Task(
                        title = "Title 1",
                        description = "Description 1",
                        isCompleted = false,
                        id = "ID 1"
                    ),
                    Task(
                        title = "Title 2",
                        description = "Description 2",
                        isCompleted = true,
                        id = "ID 2"
                    ),
                    Task(
                        title = "Title 3",
                        description = "Description 3",
                        isCompleted = true,
                        id = "ID 3"
                    ),
                    Task(
                        title = "Title 4",
                        description = "Description 4",
                        isCompleted = false,
                        id = "ID 4"
                    ),
                    Task(
                        title = "Title 5",
                        description = "Description 5",
                        isCompleted = true,
                        id = "ID 5"
                    ),
                ),
                currentFilteringLabel = R.string.label_all,
                noTasksLabel = R.string.no_tasks_all,
                noTasksIconRes = R.drawable.logo_no_fill,
                onRefresh = { },
                onTaskClick = { },
                onTaskCheckedChange = { _, _ -> },
            )
        }
    }
}

@Preview
@Composable
private fun TasksContentEmptyPreview() {
    ArchitectureTheme {
        Surface {
            TasksContent(
                loading = false,
                tasks = emptyList(),
                currentFilteringLabel = R.string.label_all,
                noTasksLabel = R.string.no_tasks_all,
                noTasksIconRes = R.drawable.logo_no_fill,
                onRefresh = {},
                onTaskClick = {},
                onTaskCheckedChange = { _, _ -> }
            )
        }
    }
}

@Preview
@Composable
private fun TasksEmptyContentPreview() {
    ArchitectureTheme {
        Surface {
            TasksEmptyContent(
                noTasksLabel = R.string.no_tasks_all,
                noTasksIconRes = R.drawable.logo_no_fill
            )
        }
    }
}

@Preview
@Composable
private fun TaskItemPreview() {
    ArchitectureTheme {
        Surface {
            TaskItem(
                task = Task(
                    title = "Title",
                    description = "Description",
                    id = "ID"
                ),
                onTaskClick = { },
                onTaskCheckedChange = {}
            )
        }
    }
}

@Preview
@Composable
private fun TaskItemCompletedPreview() {
    ArchitectureTheme {
        Surface {
            TaskItem(
                task = Task(
                    title = "Title",
                    description = "Description",
                    isCompleted = true,
                    id = "ID"
                ),
                onTaskClick = { },
                onTaskCheckedChange = { }
            )
        }
    }
}


