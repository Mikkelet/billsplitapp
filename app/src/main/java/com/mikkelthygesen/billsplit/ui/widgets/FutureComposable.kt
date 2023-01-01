package com.mikkelthygesen.billsplit.ui.widgets

import android.annotation.SuppressLint
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.*
import kotlinx.coroutines.launch

sealed class FutureState<T> {
    class Loading<T> : FutureState<T>()
    class Success<T>(val data: T) : FutureState<T>()
    class Failure<T>(val error: Throwable) : FutureState<T>()
}

@Composable
fun <T> FutureComposable(
    asyncCallback: suspend () -> T,
    content: @Composable (state: FutureState<T>, refresh: () -> Unit) -> Unit
) {
    _FutureComposable(
        asyncCallback,
        asyncCallback,
        content
    )
}

@Composable
fun <T> FutureComposable(
    asyncCallback: suspend () -> T,
    refreshCallback: suspend () -> T,
    content: @Composable (state: FutureState<T>, refresh: () -> Unit) -> Unit
) {
    _FutureComposable(
        asyncCallback,
        refreshCallback,
        content
    )
}

@Composable
@SuppressLint("ComposableNaming")
private fun <T> _FutureComposable(
    asyncCallback: suspend () -> T,
    refreshCallback: suspend () -> T,
    content: @Composable (state: FutureState<T>, refresh: () -> Unit) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var futureState: FutureState<T> by remember {
        mutableStateOf(FutureState.Loading())
    }

    fun execute(refresh: Boolean) {
        futureState = FutureState.Loading()
        coroutineScope.launch {
            val response = runCatching {
                if (refresh)
                    refreshCallback()
                else asyncCallback()
            }
            response.fold(
                onSuccess = { futureState = FutureState.Success(it) },
                onFailure = { futureState = FutureState.Failure(it) }
            )
        }
    }

    // Run on launch
    LaunchedEffect(Unit) {
        execute(false)
    }

    Crossfade(targetState = futureState) {
        content(it) {
            execute(true)
        }
    }
}