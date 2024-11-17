package com.example.architecture.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * @项目 ：architecture
 * @包名 ：com.example.architecture.data.source.local
 * @作者 : bobo
 * @日期和时间: 2024/11/17 下午1:07
 **/

/**
  *
  * @description 包含任务表的数据库。
  * @author bobo
  * @time 2024/11/17 下午1:10
  */
@Database(entities = [LocalTask::class], version = 1, exportSchema = false)
abstract class ToDoDatabase: RoomDatabase() {
    abstract fun taskDao(): TaskDao
}