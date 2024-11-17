package com.example.architecture.data

/**
 * @项目 ：architecture
 * @包名 ：com.example.architecture.data
 * @作者 : bobo
 * @日期和时间: 2024/11/17 上午11:29
 **/

/**
  *
  * @description 此类的构造函数应为“内部”，但它用于预览和测试,因此，在重构这些预览/测试之前，这是不可能的。
  * @author bobo
  * @time 2024/11/17 上午11:42
  */
data class Task(
    val title:String ="",
    val description:String="",
    val isCompleted:Boolean=false,
    val id:String,
){
    val titleForList:String
        get() = if (title.isNotEmpty()) title else description
    val isActive
        get() = !isCompleted
    val isEmpty
        get() = title.isEmpty() || description.isEmpty()
}
