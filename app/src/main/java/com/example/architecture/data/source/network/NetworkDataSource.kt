package com.example.architecture.data.source.network

/**
 * @项目 ：architecture
 * @包名 ：com.example.architecture.data.source.network
 * @作者 : bobo
 * @日期和时间: 2024/11/17 下午1:20
 **/
/**
  *
  * @description 从网络访问任务数据的主要入口点。
  * @param
  * @return
  * @author bobo
  * @time 2024/11/17 下午1:21
  */
interface NetworkDataSource {
    suspend fun loadTasks(): List<NetworkTask>
    suspend fun saveTasks(tasks: List<NetworkTask>)
}