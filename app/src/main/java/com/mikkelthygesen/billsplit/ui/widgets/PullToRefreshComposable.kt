package com.mikkelthygesen.billsplit.ui.widgets

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch

sealed class PullToRefreshState<T> {
    class Loading<T> : PullToRefreshState<T>()
    class Success<T>(val data: T) : PullToRefreshState<T>()
    class RefreshFailure<T>(val error: Throwable, val currentData: T?) : PullToRefreshState<T>()
    class InitFailure<T>(val error: Throwable) : PullToRefreshState<T>()
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T> PullToRefreshComposable(
    initialCallback: suspend () -> T,
    onRefresh: suspend () -> T,
    content: @Composable (PullToRefreshState<T>) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    var asyncState: PullToRefreshState<T> by remember {
        mutableStateOf(PullToRefreshState.Loading())
    }
    var prevSuccessData: T? by remember {
        mutableStateOf(null)
    }

    fun execute(refresh: Boolean) {
        asyncState = PullToRefreshState.Loading()
        coroutineScope.launch {
            val response = runCatching {
                if (refresh)
                    onRefresh()
                else initialCallback()
            }
            response.fold(
                onSuccess = {
                    prevSuccessData = it
                    asyncState = PullToRefreshState.Success(it)
                },
                onFailure = {
                    asyncState = if (prevSuccessData == null)
                        PullToRefreshState.InitFailure(it)
                    else PullToRefreshState.RefreshFailure(it, prevSuccessData)
                }
            )
        }
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = asyncState is PullToRefreshState.Loading,
        onRefresh = { execute(true) })

    LaunchedEffect(Unit) { execute(false) }

    Box(
        modifier = Modifier
            .pullRefresh(pullRefreshState)
    ) {
        Crossfade(targetState = asyncState) {
            LazyColumn {
                item {
                    content(it)
                }
            }
        }
        PullRefreshIndicator(
            refreshing = asyncState is PullToRefreshState.Loading,
            state = pullRefreshState,
            modifier = Modifier
                .align(Alignment.TopCenter),
        )
    }
}