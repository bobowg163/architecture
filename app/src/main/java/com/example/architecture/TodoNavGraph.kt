package com.example.architecture

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.architecture.TodoDestinationsArgs.USER_MESSAGE_ARG
import com.example.architecture.tasks.TasksScreen
import com.example.architecture.util.AppModalDrawer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * @项目 architecture
 * ＠包 com.example.architecture
 * @作者 bobo
 * @日期及日间 2024/11/25 19:17
 **/

@Composable
fun TodoNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    startDestination: String = TodoDestinations.TASKS_ROUTE,
    navActions: TodoNavigationActions = remember(navController) {
        TodoNavigationActions(navController)
    }
) {
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: startDestination
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(TodoDestinations.TASKS_ROUTE, arguments = listOf(
            navArgument(USER_MESSAGE_ARG) { type = NavType.IntType;defaultValue = 0 }
        )) { entry ->
            AppModalDrawer(
                modifier = Modifier.fillMaxWidth(),
                drawerState,
                currentRoute,
                navActions
            ) {
                AppModalDrawer(
                    drawerState = drawerState,
                    currentRoute = currentRoute,
                    navigationActions = navActions
                ) {
                    TasksScreen(
                        userMessage = entry.arguments?.getInt(USER_MESSAGE_ARG)!!,
                        onUserMessageDisplayed = { entry.arguments?.putInt(USER_MESSAGE_ARG, 0) },
                        onAddTask = { navActions.navigateToAddEditTask(R.string.add_task, null) },
                        onTaskClick = { task -> navActions.navigateToTaskDetail(taskId = task.id) },
                        openDrawer = { coroutineScope.launch { drawerState.open() } }
                    )
                }
            }
        }
    }
}