package com.example.architecture.data

import com.example.architecture.data.source.local.TaskDao
import com.example.architecture.data.source.network.NetworkDataSource
import com.example.architecture.di.ApplicationScope
import com.example.architecture.di.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
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
):TaskRepository {
    override fun getTasksStream(): Flow<List<Task>> {
       return localDataSource.observeAll().map { tasks->
           withContext(dispatcher){
               tasks.toExternal()
           }
       }
    }

    override suspend fun getTasks(forceUpdate: Boolean): List<Task> {
        TODO("Not yet implemented")
    }

    override suspend fun refresh() {
        TODO("Not yet implemented")
    }

    override fun getTaskStream(taskId: String): Flow<Task?> {
        TODO("Not yet implemented")
    }

    override suspend fun getTask(taskId: String, forceUpdate: Boolean): Task? {
        TODO("Not yet implemented")
    }

    override suspend fun refreshTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun createTask(title: String, description: String): String {
        TODO("Not yet implemented")
    }

    override suspend fun updateTask(taskId: String, title: String, description: String) {
        TODO("Not yet implemented")
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


}