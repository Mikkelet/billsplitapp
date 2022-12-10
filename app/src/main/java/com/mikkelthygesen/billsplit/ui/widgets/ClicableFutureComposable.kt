package com.mikkelthygesen.billsplit.ui.widgets

import androidx.compose.material.Text
import androidx.compose.runtime.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun <T> ClickableFutureComposable(
    asyncCallback: suspend () -> T,
    loadingComposable: @Composable () -> Unit = { LoadingView() },
    errorComposable: @Composable (Throwable) -> Unit = {
        Timber.e(it)
        Text(text = it.toString())
    },
    onSuccess: (T?) -> Unit,
    clickableComposable: @Composable (() -> Unit) -> Unit,
) {
    var asyncState: AsyncState<T> by remember {
        mutableStateOf(AsyncState.Ready())
    }
    val coroutineScope = rememberCoroutineScope()

    val callback: () -> Unit = {
        coroutineScope.launch {
            asyncState = AsyncState.Loading()
            delay(2000L)
            val response = runCatching { asyncCallback() }
            response.fold(
                onSuccess = { asyncState = AsyncState.Success(it) },
                onFailure = { asyncState = AsyncState.Failure(it) }
            )
        }
    }

    when (val state = asyncState) {
        is AsyncState.Ready<T> -> clickableComposable(callback)
        is AsyncState.Loading<T> -> loadingComposable()
        is AsyncState.Failure<T> -> errorComposable(state.error)
        is AsyncState.Success<T> -> when (state.data) {
            else -> {
                onSuccess(state.data)
                clickableComposable(callback)
            }
        }
    }
}