package com.mikkelthygesen.billsplit.ui.widgets

import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun <T> ClickableFutureComposable(
    asyncCallback: suspend () -> T,
    onError: (Throwable) -> Unit = { Timber.e(it) },
    onSuccess: (T?) -> Unit = {},
    loadingComposable: @Composable () -> Unit = { LoadingView() },
    clickableComposable: @Composable (() -> Unit) -> Unit,
) {
    var asyncState: AsyncState<T> by remember {
        mutableStateOf(AsyncState.Ready())
    }
    val coroutineScope = rememberCoroutineScope()

    val callback: () -> Unit = {
        coroutineScope.launch {
            asyncState = AsyncState.Loading()
            val response = runCatching { asyncCallback() }
            response.fold(
                onSuccess = {
                    onSuccess(it)
                    asyncState = AsyncState.Success(it)
                },
                onFailure = {
                    onError(it)
                    asyncState = AsyncState.Ready()
                }
            )
        }
    }

    // Clickable state can only be ready or loading.
    // Success and failure should be handled by the relevant callbacks
    when (asyncState) {
        is AsyncState.Loading<T> -> loadingComposable()
        else -> clickableComposable(callback)
    }
}