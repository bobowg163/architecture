package com.example.architecture.util

/**
 * @项目 ：architecture
 * @包名 ：com.example.architecture.util
 * @作者 : bobo
 * @日期和时间: 2024/11/23 18:25
 **/

/**
 *
 * @描述 保存加载信号或异步操作结果的通用类。
 * @参数
 * @返回
 * @作者 bobo
 * @日期及时间 2024/11/23 18:26
 */
sealed class Async<out T> {
    object Loading : Async<Nothing>()
    data class Success<out T>(val data: T) : Async<T>()
    data class Error(val errorMessage: Int) : Async<Nothing>()

}