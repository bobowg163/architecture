package com.example.architecture.data

import com.example.architecture.data.source.local.TaskDao
import com.example.architecture.data.source.network.NetworkDataSource
import com.example.architecture.di.ApplicationScope
import com.example.architecture.di.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @项目 ：architecture
 * @包名 ：com.example.architecture.data
 * @作者 : bobo
 * @日期和时间: 2024/11/17 下午1:52
 **/

/**
 *
 * @描述 [TaskRepository] ​​的默认实现。管理任务数据的单一入口点。
 * 用于长时间运行或复杂操作的调度程序，例如 ID
 * 生成或映射多个模型。
 * @参数 网络数据源
 * @返回 向网络发送数据。
 * @作者 bobo
 * @日期及时间 2024/11/18 22:56
 */
@Singleton
class DefaultTaskRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val localDataSource: TaskDao,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
    @ApplicationScope private val scope: CoroutineScope,
) : TaskRepository {
    override fun getTasksStream(): Flow<List<Task>> {
        return localDataSource.observeAll().map { tasks ->
            withContext(dispatcher) {
                tasks.toExternal()
            }
        }
    }

    override suspend fun getTasks(forceUpdate: Boolean): List<Task> {
        if (forceUpdate) {
            refresh()
        }
        return withContext(dispatcher) {
            localDataSource.getAll().toExternal()
        }
    }

    /**
     *
     * @描述 以下方法从网络加载任务（刷新）并将任务保存到网络。实际应用可能希望进行适当的同步，而不是下面的“单向同步所有内容”请注意，刷新操作是暂停函数（强制调用者等待），而保存操作不是。它会立即返回，因此调用者不必等待.
     * 删除本地数据源中的所有内容，并将其替换为网络数据源中的所有内容。
     * *
     * * 如果批量 `toLocal` 映射操作很复杂，则此处使用 `withContext`。
     * @作者 bobo
     * @日期及时间 2024/11/23 09:26
     */
    override suspend fun refresh() {
        withContext(dispatcher) {
            val remoteTasks = networkDataSource.loadTasks()
            localDataSource.deleteAll()
            localDataSource.upsertAll(remoteTasks.toLocal())
        }
    }

    override fun getTaskStream(taskId: String): Flow<Task?> {
        return localDataSource.observeById(taskId).map { task ->
            task.toExternal()
        }
    }

    /**
     *
     * @描述 获取具有指定 ID 的任务。如果找不到该任务，则返回 null。
     * @参数 任务的ID,forceUpdate - 如果应首先从网络数据源更新任务，则为 true。
     * @作者 bobo
     * @日期及时间 2024/11/23 09:30
     */
    override suspend fun getTask(taskId: String, forceUpdate: Boolean): Task? {
        if (forceUpdate) {
            refresh()
        }
        return localDataSource.getById(taskId)?.toExternal()
    }

    override suspend fun refreshTask(taskId: String) {
        refresh()
    }

    override suspend fun createTask(title: String, description: String): String {
        val taskId = withContext(dispatcher) {
            UUID.randomUUID().toString()
        }
        val task = Task(
            title = title,
            description = description,
            id = taskId,
            isCompleted = false,
        )
        localDataSource.upsert(task.toLocal())
        saveTasksToNetwork()
        return taskId
    }

    override suspend fun updateTask(taskId: String, title: String, description: String) {
        val task = getTask(taskId)?.copy(title = title,description=description)?:throw Exception("Task (id $taskId) not found")
        localDataSource.upsert(task.toLocal())
        saveTasksToNetwork()
    }

    override suspend fun completeTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun activateTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun clearCompletedTasks() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllTasks() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTask(taskId: String) {
        TODO("Not yet implemented")
    }

    private fun saveTasksToNetwork() {
        scope.launch {
            try {
                val localTasks = localDataSource.getAll()
                val networkTasks = withContext(dispatcher) {
                    localTasks.toNetwork()
                }
                networkDataSource.saveTasks(networkTasks)
            } catch (e: Exception) {
                // 处理保存任务到网络时发生的异常
                // 在实际应用中，您可以处理异常，例如通过将 `networkStatus` 流
                // 公开到应用级 UI 状态持有者，然后该持有者可以显示 Toast 消息。
            }
        }
    }

}