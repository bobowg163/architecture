package com.example.architecture.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.architecture.R
import com.example.architecture.TodoDestinations
import com.example.architecture.TodoNavigationActions
import com.example.architecture.ui.theme.colorPrimaryDark
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * @项目 ：architecture
 * @包名 ：com.example.architecture.util
 * @作者 : bobo
 * @日期和时间: 2024/11/23 21:00
 **/

@Composable
fun AppModalDrawer(
    modifier: Modifier = Modifier,
    drawerState: DrawerState,
    currentRoute: String,
    navigationActions: TodoNavigationActions,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        modifier = modifier,
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                currentRoute = currentRoute,
                navigateToTasks = {
                    navigationActions.navigateToTasks()
                },
                navigateToStatistics = { navigationActions.navigateToStatistics() },
                closeDrawer = { coroutineScope.launch { drawerState.close() } }
            )
        }
    ) {
        content()
    }
}

@Composable
private fun AppDrawer(
    modifier: Modifier = Modifier,
    currentRoute: String,
    navigateToTasks: () -> Unit,
    navigateToStatistics: () -> Unit,
    closeDrawer: () -> Unit,
) {
    Column(modifier = modifier.fillMaxSize()) {
        DrawerHeader()
        DrawerButton(
            painter = painterResource(id = R.drawable.ic_list),
            label = stringResource(id = R.string.list_title),
            isSelected = currentRoute == TodoDestinations.TASKS_ROUTE,
            action = {
                navigateToTasks()
                closeDrawer()
            }
        )
        DrawerButton(
            painter = painterResource(id = R.drawable.ic_statistics),
            label = stringResource(id = R.string.statistics_title),
            isSelected = currentRoute == TodoDestinations.STATISTICS_ROUTE,
            action = {
                navigateToStatistics()
                closeDrawer()
            }
        )
    }

}

@Composable
private fun DrawerButton(
    modifier: Modifier = Modifier,
    painter: Painter,
    label: String,
    isSelected: Boolean,
    action: () -> Unit
) {
    val tintColor = if (isSelected) {
        MaterialTheme.colorScheme.secondary
    } else {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    }
    TextButton(
        onClick = action,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.horizontal_margin))
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(painter = painter, contentDescription = null, tint = tintColor)
            Spacer(Modifier.width(16.dp))
            Text(text = label, style = MaterialTheme.typography.bodySmall, color = tintColor)
        }
    }

}


@Composable
private fun DrawerHeader(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .background(colorPrimaryDark)
            .height(dimensionResource(id = R.dimen.header_height))
            .padding(dimensionResource(id = R.dimen.header_padding))
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_no_fill),
            contentDescription =
            stringResource(id = R.string.tasks_header_image_content_description),
            modifier = Modifier.width(dimensionResource(id = R.dimen.header_image_width))
        )
        Text(
            text = stringResource(id = R.string.navigation_view_header_title),
            color = MaterialTheme.colorScheme.surface
        )
    }
}

@Preview
@Composable
private fun PreviewAppDrawer() {
    MaterialTheme {
        Surface {
            AppDrawer(
                currentRoute = TodoDestinations.TASKS_ROUTE,
                navigateToStatistics = {},
                navigateToTasks = {},
                closeDrawer = {}
            )
        }
    }
}