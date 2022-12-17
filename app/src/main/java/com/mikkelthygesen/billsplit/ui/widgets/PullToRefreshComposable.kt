package com.mikkelthygesen.billsplit.ui.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T> PullToRefreshComposable(
    initialCallback: suspend () -> T,
    onRefresh: suspend () -> T,
    loadingComposable: @Composable () -> Unit = { LoadingView() },
    onError: (Throwable) -> Unit = {},
    errorComposable: @Composable (Throwable) -> Unit = {
        Text(text = it.toString())
    },
    successComposable: @Composable (T) -> Unit,
) {

    var asyncState: AsyncState<T> by remember {
        mutableStateOf(AsyncState.Loading())
    }

    val coroutineScope = rememberCoroutineScope()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = asyncState is AsyncState.Loading,
        onRefresh = {
            asyncState = AsyncState.Loading()
            coroutineScope.launch {
                val response = runCatching { onRefresh() }
                response.fold(
                    onSuccess = {
                        asyncState = AsyncState.Success(it)
                    },
                    onFailure = {
                        onError(it)
                        asyncState = AsyncState.Failure(it)
                    }
                )
            }
        })

    LaunchedEffect(Unit) {
        asyncState = AsyncState.Loading()
        val response = runCatching { initialCallback() }
        response.fold(
            onSuccess = { asyncState = AsyncState.Success(it) },
            onFailure = {
                onError(it)
                asyncState = AsyncState.Failure(it)
            }
        )
    }

    when (val state = asyncState) {
        is AsyncState.Failure<T> -> errorComposable(state.error)
        is AsyncState.Success<T> ->
            Box(
                modifier = Modifier
                    .pullRefresh(pullRefreshState)
            ) {
                LazyColumn {
                    item {
                        successComposable(state.data)
                    }
                }
                PullRefreshIndicator(
                    refreshing = asyncState is AsyncState.Loading,
                    state = pullRefreshState,
                    modifier = Modifier
                        .align(Alignment.TopCenter),
                )
            }
        else -> loadingComposable()
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    PullToRefreshComposable(initialCallback = {
        delay(2000L)
    }, onRefresh = {
        delay(2000L)
    }) {
        Text("success!")
    }
}