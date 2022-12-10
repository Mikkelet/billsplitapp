package com.mikkelthygesen.billsplit.ui.widgets

import androidx.compose.material.Text
import androidx.compose.runtime.*
import timber.log.Timber

sealed class AsyncState<T> {
    class Loading<T> : AsyncState<T>()
    class Success<T>(val data: T) : AsyncState<T>()
    class Failure<T>(val error: Throwable) : AsyncState<T>()
}

@Composable
fun <T> FutureComposable(
    asyncCallback: suspend () -> T,
    loadingComposable: @Composable () -> Unit = { LoadingView() },
    errorComposable: @Composable (Throwable) -> Unit = {
        Timber.e(it)
        Text(text = it.toString())
    },
    successComposable: @Composable (T) -> Unit,
) {
    var asyncState: AsyncState<T> by remember {
        mutableStateOf(AsyncState.Loading())
    }
    LaunchedEffect(Unit) {
        asyncState = AsyncState.Loading()
        val response = runCatching { asyncCallback() }
        response.fold(
            onSuccess = { asyncState = AsyncState.Success(it) },
            onFailure = { asyncState = AsyncState.Failure(it) }
        )
    }

    when (val state = asyncState) {
        is AsyncState.Loading<T> -> loadingComposable()
        is AsyncState.Failure<T> -> errorComposable(state.error)
        is AsyncState.Success<T> -> when (state.data) {
            null -> Unit
            else -> successComposable(state.data)
        }
    }
}