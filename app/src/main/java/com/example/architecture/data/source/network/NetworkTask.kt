package com.example.architecture.data.source.network

/**
 * @项目 ：architecture
 * @包名 ：com.example.architecture.data.source.network
 * @作者 : bobo
 * @日期和时间: 2024/11/17 下午1:16
 **/

/**
  *
  * @description 用于表示从网络获得的任务的内部模型。这仅在数据层内部使用.
  * 请参阅 ModelMappingExt.kt 以了解用于将此模型转换为其他模型的映射函数。
  *
  * @author bobo
  * @time 2024/11/17 下午1:19
  */
data class NetworkTask(
    val id: String,
    val title: String,
    val shortDescription: String,
    val priority: Int? = null,
    val status:TaskStatus = TaskStatus.ACTIVE
)

enum class TaskStatus{
    ACTIVE,
    COMPLETE
}
