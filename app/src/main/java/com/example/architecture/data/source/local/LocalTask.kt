package com.example.architecture.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @项目 ：architecture
 * @包名 ：com.example.architecture.data.source.local
 * @作者 : bobo
 * @日期和时间: 2024/11/17 下午12:12
 **/

@Entity(
    tableName = "task"
)
data class LocalTask(
    @PrimaryKey
    val id: String,
    var title: String,
    var description: String,
    var isCompleted: Boolean
)
