package com.example.architecture.data.source.network

import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
/**
 * @项目 ：architecture
 * @包名 ：com.example.architecture.data.source.network
 * @作者 : bobo
 * @日期和时间: 2024/11/17 下午1:21
 **/

/**
  *
  * @description 使用 Hilt 实现依赖项注入
 * @param
  * @return
  * @author bobo
  * @time 2024/11/17 下午1:40
  */
class TaskNetworkDataSource @Inject constructor():NetworkDataSource {

    private val accessMutex = Mutex()
    private var tasks = listOf(
        NetworkTask(
            id = "PISA",
            title = "Build tower in Pisa",
            shortDescription = "Ground looks good, no foundation work required."
        ),
        NetworkTask(
            id = "TACOMA",
            title = "Finish bridge in Tacoma",
            shortDescription = "Found awesome girders at half the cost!"
        )
    )
    override suspend fun loadTasks(): List<NetworkTask> = accessMutex.withLock {
        delay(SERVICE_LATENCY_IN_MILLIS)
        return tasks
    }

    override suspend fun saveTasks(tasks: List<NetworkTask>) = accessMutex.withLock {
        delay(SERVICE_LATENCY_IN_MILLIS)
        this.tasks = tasks
    }
}

private const val SERVICE_LATENCY_IN_MILLIS = 2000L