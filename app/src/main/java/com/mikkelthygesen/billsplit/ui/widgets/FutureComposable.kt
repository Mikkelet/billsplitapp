package com.mikkelthygesen.billsplit.ui.widgets

import androidx.compose.material.Text
import androidx.compose.runtime.*
import kotlinx.coroutines.launch

sealed class AsyncState<T> {
    class Ready<T> : AsyncState<T>()
    class Loading<T> : AsyncState<T>()
    class Success<T>(val data: T) : AsyncState<T>()
    class Failure<T>(val error: Throwable) : AsyncState<T>()
}

@Composable
fun <T> FutureComposable(
    asyncCallback: suspend () -> T,
    loadingComposable: @Composable () -> Unit = { LoadingView() },
    onError: (Throwable) -> Unit = {},
    errorComposable: @Composable (Throwable) -> Unit = {
        Text(text = it.toString())
    },
    successComposable: @Composable (T, (suspend ()->T) -> Unit) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    var asyncState: AsyncState<T> by remember {
        mutableStateOf(AsyncState.Loading())
    }

    fun loadData(callback: suspend () -> T) {
        coroutineScope.launch {
            asyncState = AsyncState.Loading()
            val response = runCatching { callback() }
            response.fold(
                onSuccess = { asyncState = AsyncState.Success(it) },
                onFailure = {
                    onError(it)
                    asyncState = AsyncState.Failure(it)
                }
            )
        }
    }
    LaunchedEffect(Unit) { loadData(asyncCallback) }

    when (val state = asyncState) {
        is AsyncState.Failure<T> -> errorComposable(state.error)
        is AsyncState.Success<T> -> when (state.data) {
            null -> Unit
            else -> successComposable(state.data) { refreshCallback -> loadData(refreshCallback) }
        }
        else -> loadingComposable()
    }
}