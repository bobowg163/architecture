package com.example.architecture.statistics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.architecture.R
import com.example.architecture.ui.theme.ArchitectureTheme
import com.example.architecture.util.LoadingContent
import com.example.architecture.util.StatisticsTopAppBar

/**
 * @项目 architecture
 * ＠包 com.example.architecture.statistics
 * @作者 bobo
 * @日期及日间 2024/11/28 19:14
 **/

@Composable
fun StatisticsScreen(
    modifier: Modifier = Modifier,
    openDrawer: () -> Unit,
    viewModel: StatisticsViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { StatisticsTopAppBar(openDrawer) }
    ) { paddingValues ->
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        StatisticsContent(
            loading = uiState.isLoading,
            empty = uiState.isEmpty,
            activeTasksPercent = uiState.activeTasksPercent,
            completedTasksPercent = uiState.completedTasksPercent,
            onRefresh = { viewModel.refresh() },
            modifier = modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun StatisticsContent(
    loading: Boolean,
    empty: Boolean,
    activeTasksPercent: Float,
    completedTasksPercent: Float,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,

    ) {
    val commonModifier =
        modifier
            .fillMaxWidth()
            .padding(all = dimensionResource(id = R.dimen.horizontal_margin))
    LoadingContent(
        loading = loading,
        empty = empty,
        onRefresh = onRefresh,
        modifier = modifier,
        emptyContent = {
            Text(
                text = stringResource(id = R.string.statistics_no_tasks),
                modifier = commonModifier
            )
        }
    ) {
        Column(
            commonModifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            if (!loading) {
                Text(stringResource(id = R.string.statistics_active_tasks, activeTasksPercent))
                Text(
                    stringResource(
                        id = R.string.statistics_completed_tasks,
                        completedTasksPercent
                    )
                )
            }
        }
    }

}

@Preview
@Composable
fun StatisticsContentPreview() {
    ArchitectureTheme {
        Surface {
            StatisticsContent(
                loading = false,
                empty = false,
                activeTasksPercent = 80f,
                completedTasksPercent = 20f,
                onRefresh = { }
            )
        }
    }
}

@Preview
@Composable
fun StatisticsContentEmptyPreview() {
    ArchitectureTheme {
        Surface {
            StatisticsContent(
                loading = false,
                empty = true,
                activeTasksPercent = 0f,
                completedTasksPercent = 0f,
                onRefresh = { }
            )
        }
    }
}
