package com.example.architecture

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.architecture.TodoDestinationsArgs.TASK_ID_ARG
import com.example.architecture.TodoDestinationsArgs.TITLE_ARG
import com.example.architecture.TodoDestinationsArgs.USER_MESSAGE_ARG
import com.example.architecture.TodoScreens.ADD_EDIT_TASK_SCREEN
import com.example.architecture.TodoScreens.STATISTICS_SCREEN
import com.example.architecture.TodoScreens.TASKS_SCREEN
import com.example.architecture.TodoScreens.TASK_DETAIL_SCREEN

/**
 * @项目 ：architecture
 * @包名 ：com.example.architecture
 * @作者 : bobo
 * @日期和时间: 2024/11/23 21:26
 **/

/**
 *
 * @描述 中使用的屏幕
 * @参数
 * @返回
 * @作者 bobo
 * @日期及时间 2024/11/23 22:00
 */
private object TodoScreens {
    const val TASKS_SCREEN = "tasks"
    const val STATISTICS_SCREEN = "statistics"
    const val TASK_DETAIL_SCREEN = "task"
    const val ADD_EDIT_TASK_SCREEN = "addEditTask"
}

object TodoDestinationsArgs {
    const val USER_MESSAGE_ARG = "userMessage"
    const val TASK_ID_ARG = "taskId"
    const val TITLE_ARG = "title"
}

object TodoDestinations {
    const val TASKS_ROUTE = "$TASKS_SCREEN?$USER_MESSAGE_ARG={$USER_MESSAGE_ARG}"
    const val STATISTICS_ROUTE = STATISTICS_SCREEN
    const val TASK_DETAIL_ROUTE = "$TASK_DETAIL_SCREEN/{$TASK_ID_ARG}"
    const val ADD_EDIT_TASK_ROUTE = "$ADD_EDIT_TASK_SCREEN/{$TITLE_ARG}?$TASK_ID_ARG={$TASK_ID_ARG}"
}

class TodoNavigationActions(private val navController: NavHostController) {

    fun navigateToTasks(userMessage: Int = 0) {
        val navigatesFromDrawer = userMessage == 0
        navController.navigate(
            TASKS_SCREEN.let {
                if (userMessage != 0) "$it?$USER_MESSAGE_ARG=$userMessage" else it
            }
        ){
            popUpTo(navController.graph.findStartDestination().id){
                inclusive = !navigatesFromDrawer
                saveState = navigatesFromDrawer
            }
            launchSingleTop = true
            restoreState = navigatesFromDrawer
        }
    }

    fun navigateToStatistics(){

    }
}