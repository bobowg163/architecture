package com.example.architecture.tasks

/**
 * @项目 architecture
 * ＠包 com.example.architecture.tasks
 * @作者 bobo
 * @日期及日间 2024/11/26 18:51
 **/

/*
 *与任务列表中的过滤器微调器一起使用。
 */
enum class TasksFilterType {
    /*
    *不过滤任务。
     */
    ALL_TASKS,
    /*
   *仅过滤活动（尚未完成）的任务。
    */
    ACTIVE_TASKS,
    /*
   *仅过滤已完成的任务。
    */
    COMPLETED_TASKS
}