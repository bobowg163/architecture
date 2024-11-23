package com.example.architecture.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

/**
 * @项目 ：architecture
 * @包名 ：com.example.architecture.util
 * @作者 : bobo
 * @日期和时间: 2024/11/23 18:28
 **/

/**
 *
 * @描述 显示初始空状态或滑动以刷新内容。
 * @作者 bobo
 * @日期及时间 2024/11/23 18:32
 */
@Composable
fun LoadingContent(
    loading: Boolean,
    empty: Boolean,
    emptyContent: @Composable () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    if (empty) {
        emptyContent()
    } else {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = loading),
            onRefresh = onRefresh,
            modifier = modifier,
            content = content,
        )
    }

}