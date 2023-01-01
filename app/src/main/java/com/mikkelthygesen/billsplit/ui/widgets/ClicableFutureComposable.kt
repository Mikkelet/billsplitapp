package com.mikkelthygesen.billsplit.ui.widgets

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.*
import kotlinx.coroutines.launch

sealed class TriggerFutureState<T> {
    class Ready<T> : TriggerFutureState<T>()
    class Loading<T> : TriggerFutureState<T>()
    class Success<T>(val data: T) : TriggerFutureState<T>()
    class Failure<T>(val error: Throwable) : TriggerFutureState<T>()
}

@Composable
fun <T> TriggerFutureComposable(
    onClickAsync: suspend () -> T,
    content: @Composable (TriggerFutureState<T>, () -> Unit) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var asyncState: TriggerFutureState<T> by remember {
        mutableStateOf(TriggerFutureState.Ready())
    }

    fun callback() {
        coroutineScope.launch {
            asyncState = TriggerFutureState.Loading()
            val response = runCatching { onClickAsync() }
            response.fold(
                onSuccess = {
                    asyncState = TriggerFutureState.Success(it)
                },
                onFailure = {
                    asyncState = TriggerFutureState.Failure(it)
                }
            )
        }
    }

    Crossfade(targetState = asyncState) {
        content(it) {
            callback()
        }
    }
}