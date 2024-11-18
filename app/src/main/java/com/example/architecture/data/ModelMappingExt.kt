package com.example.architecture.data

import com.example.architecture.data.source.local.LocalTask
import com.example.architecture.data.source.network.NetworkTask
import com.example.architecture.data.source.network.TaskStatus

/**
 * @项目 ：architecture
 * @包名 ：com.example.architecture.data
 * @作者 : bobo
 * @日期和时间: 2024/11/18 23:14
 **/

/**
 * 数据模型映射扩展函数。有三种模型类型：
 *
 * - Task：暴露给架构中其他层的外部模型。
 * 使用 `toExternal` 获取。
 *
 * - NetworkTask：用于表示来自网络的任务的内部模型。使用
 * `toNetwork` 获取。
 *
 * - LocalTask​​：用于表示本地存储在数据库中的任务的内部模型。使用
 * `toLocal` 获取。
 *
 */

//从外部到本地
fun Task.toLocal() = LocalTask(
    id = id,
    title = title,
    description = description,
    isCompleted = isCompleted,
)

fun List<Task>.toLocal() = map(Task::toLocal)

//本地到外部
fun LocalTask.toExternal() = Task(
    id = id,
    title = title,
    description = description,
    isCompleted = isCompleted,
)
/**
  *
  * @描述 // 注意：JvmName 用于为每个同名的扩展函数提供唯一的名称。
 * // 如果没有这个，类型擦除将导致编译器错误，因为这些方法在 JVM 上具有相同的
 * // 签名。
  * @参数
  * @返回
  * @作者 bobo
  * @日期及时间 2024/11/18 23:23
  */
@JvmName("localToExternal")
fun List<LocalTask>.toExternal() = map(LocalTask::toExternal)

//网络到本地
fun NetworkTask.toLocal() = LocalTask(
    id = id,
    title = title,
    description = shortDescription,
    isCompleted = (status == TaskStatus.COMPLETE),
)

@JvmName("networkToLocal")
fun List<NetworkTask>.toLocal() = map(NetworkTask::toLocal)

// 本地到网络
fun LocalTask.toNetwork() = NetworkTask(
    id = id,
    title = title,
    shortDescription = description,
    status = if (isCompleted) { TaskStatus.COMPLETE } else { TaskStatus.ACTIVE }
)

fun List<LocalTask>.toNetwork() = map(LocalTask::toNetwork)

// 网络外部
fun Task.toNetwork() = toLocal().toNetwork()

@JvmName("externalToNetwork")
fun List<Task>.toNetwork() = map(Task::toNetwork)

// 外部网络
fun NetworkTask.toExternal() = toLocal().toExternal()

@JvmName("networkToExternal")
fun List<NetworkTask>.toExternal() = map(NetworkTask::toExternal)