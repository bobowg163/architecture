package com.example.architecture.data

import kotlinx.coroutines.flow.Flow

/**
 * @项目 ：architecture
 * @包名 ：com.example.architecture.data
 * @作者 : bobo
 * @日期和时间: 2024/11/17 20:28
 **/

/**
 *
 * @description 数据层的接口
 * @param
 * @return
 * @author bobo
 * @time 2024/11/17 20:30
 */
interface TaskRepository {

    fun getTasksSteam(): Flow<List<Task>>

    suspend fun getTasks(forceUpdate: Boolean = false): List<Task>

    suspend fun refresh()

    fun getTaskStream(taskId: String): Flow<Task?>

    suspend fun getTask(taskId: String, forceUpdate: Boolean = false): Task?

    suspend fun refreshTask(taskId: String)

    suspend fun createTask(title: String, description: String): String

    suspend fun updateTask(taskId: String, title: String, description: String)

    suspend fun completeTask(taskId: String)

    suspend fun activateTask(taskId: String)

    suspend fun clearCompletedTasks()

    suspend fun deleteAllTasks()

    suspend fun deleteTask(taskId: String)
}