package com.example.architecture.data

import com.example.architecture.data.source.local.TaskDao
import com.example.architecture.data.source.network.NetworkDataSource
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @项目 ：architecture
 * @包名 ：com.example.architecture.data
 * @作者 : bobo
 * @日期和时间: 2024/11/17 下午1:52
 **/
@Singleton
class DefaultTaskRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val localDataSource: TaskDao,
) {

}