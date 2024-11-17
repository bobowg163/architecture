package com.example.architecture.data.source.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

/**
 * @项目 ：architecture
 * @包名 ：com.example.architecture.data.source.local
 * @作者 : bobo
 * @日期和时间: 2024/11/17 下午12:24
 **/

/**
  *
  * @description 任务表的数据访问对象。
  * @param
  * @return
  * @author bobo
  * @time 2024/11/17 下午12:26
  */
@Dao
interface TaskDao {
    /**
      *
      * @description 观察任务列表
      * @param 
      * @return 所有任务
      * @author bobo
      * @time 2024/11/17 下午12:27
      */
    @Query("SELECT * FROM task")
    fun observeAll():Flow<List<LocalTask>>

    /**
      *
      * @description 观察单一任务。
      * @param taskId 任务 ID
      * @return 具有taskId的任务。
      * @author bobo
      * @time 2024/11/17 下午12:46
      */
    @Query("SELECT * FROM task WHERE id=:taskId")
    fun observeById(taskId:String):Flow<LocalTask>

    /**
      *
      * @description 从任务表中选择所有任务.
      * @param
      * @return
      * @author bobo
      * @time 2024/11/17 下午12:48
      */
    @Query("SELECT * FROM task")
    suspend fun getAll():List<LocalTask>

    /**
      *
      * @description 观察单一任务。
      * @param taskId 任务 ID
      * @return 具有taskId的任务。
      * @author bobo
      * @time 2024/11/17 下午12:49
      */
    @Query("SELECT * FROM task WHERE id=:taskId")
    suspend fun getById(taskId: String):LocalTask?

    /**
      *
      * @description 在数据库中插入或更新任务。如果任务已存在，则替换它。
      * @param task 要插入或更新的任务。
      * @author bobo
      * @time 2024/11/17 下午12:51
      */
    @Upsert
    suspend fun upsert(task: LocalTask)

    /**
      *
      * @description 在数据库中插入或更新任务。如果任务已存在，则替换它。
      * @param "任务要插入或更新的任务"。
      * @author bobo
      * @time 2024/11/17 下午12:52
      */
    @Upsert
    suspend fun upsertAll(tasks: List<LocalTask>)

    /**
      *
      * @description 更新任务的完整状态
      * @param 'taskId' 任务的id
      * @param '已完成状态待更新'
      * @author bobo
      * @time 2024/11/17 下午12:53
      */
    @Query("UPDATE task SET isCompleted = :completed WHERE id = :taskId")
    suspend fun updateCompleted(taskId: String, completed: Boolean)

    /**
      *
      * @description 根据 ID 删除任务。
      * @return 删除的任务数。该值应始终为 1。
      * @author bobo
      * @time 2024/11/17 下午1:03
      */
    @Query("DELETE FROM task WHERE id = :taskId")
    suspend fun deleteById(taskId: String):Int

    /**
      *
      * @description 删除所有的数据
      * @param
      * @return
      * @author bobo
      * @time 2024/11/17 下午1:05
      */
    @Query("DELETE FROM task")
    suspend fun deleteAll()

    /**
      *
      * @description 从表中删除所有已完成的任务。
      * @param
      * @return
      * @author bobo
      * @time 2024/11/17 下午1:06
      */
    @Query("DELETE FROM task WHERE isCompleted = 1")
    suspend fun deleteCompleted():Int
}