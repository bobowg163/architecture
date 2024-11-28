package com.example.architecture.statistics

import com.example.architecture.data.Task

/**
 * @项目 architecture
 * ＠包 com.example.architecture.statistics
 * @作者 bobo
 * @日期及日间 2024/11/28 20:24
 **/

data class StatsResult(val activeTasksPercent: Float, val completedTasksPercent: Float)

internal fun getActiveAndCompletedStats(tasks: List<Task>): StatsResult {
    return if (tasks.isEmpty()) {
        StatsResult(0f, 0f)
    } else {
        val totalTasks = tasks.size
        val numberOfActiveTasks = tasks.count { it.isActive }
        StatsResult(
            activeTasksPercent = 100f * numberOfActiveTasks / tasks.size,
            completedTasksPercent = 100f * (totalTasks - numberOfActiveTasks) / tasks.size
        )
    }
}