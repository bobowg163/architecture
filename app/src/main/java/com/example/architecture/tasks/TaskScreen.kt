package com.example.architecture.tasks

import androidx.annotation.StringRes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.architecture.data.Task

/**
 * @项目 architecture
 * ＠包 com.example.architecture.tasks
 * @作者 bobo
 * @日期及日间 2024/11/25 19:55
 **/


@Composable
fun TasksScreen(
    modifier: Modifier = Modifier,
    @StringRes userMessage:Int,
    onAddTask:() -> Unit,
    onTaskClick:(Task) -> Unit,
    onUserMessageDisplayed:() -> Unit,
    openDrawer:() -> Unit,
    viewModel:TasksViewModel = hiltViewModel(),
) {

}