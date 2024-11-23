package com.example.architecture.util

import kotlinx.coroutines.flow.SharingStarted

/**
 * @项目 ：architecture
 * @包名 ：com.example.architecture.util
 * @作者 : bobo
 * @日期和时间: 2024/11/23 18:40
 **/

private const val StopTimeoutMillis:Long = 5000

/**
  *
  * @描述 shareIn 和 stateIn 操作符中启动和停止共享协程的策略。
 * 该函数式接口提供了一组内置策略：Eagerly、Lazily、WhileSubscribed，并且通过实现该接口的 command 函数支持自定义策略。
 * 例如，可以定义一个自定义策略，该策略仅在订阅者数量超过给定阈值时才启动上游，并将其作为 SharingStarted 上的扩展伴侣，使其看起来像使用站点上的内置策略：
  * @参数
  * @返回
  * @作者 bobo
  * @日期及时间 2024/11/23 20:24
  */
val WhileUiSubscribed: SharingStarted = SharingStarted.WhileSubscribed(StopTimeoutMillis)