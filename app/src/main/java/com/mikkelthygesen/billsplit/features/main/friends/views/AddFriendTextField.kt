package com.mikkelthygesen.billsplit.features.main.friends.views

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.matchesEmail
import com.mikkelthygesen.billsplit.domain.models.Friend
import com.mikkelthygesen.billsplit.domain.models.Person
import com.mikkelthygesen.billsplit.features.main.friends.FriendsViewModel
import com.mikkelthygesen.billsplit.ui.widgets.TriggerFutureComposable
import com.mikkelthygesen.billsplit.ui.widgets.SimpleIconButton
import com.mikkelthygesen.billsplit.ui.widgets.TriggerFutureState
import io.ktor.utils.io.core.*

@Composable
fun AddFriendEmailTextField() {
    val friendsViewModel: FriendsViewModel = viewModel()
    _AddFriendTextField {
        friendsViewModel.addFriend(it.trim().lowercase())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@SuppressLint("ComposableNaming")
fun _AddFriendTextField(
    onAddFriend: suspend (String) -> Friend
) {
    var addFriendTextFieldValue by remember {
        mutableStateOf("")
    }
    var errorMessage by remember {
        mutableStateOf("")
    }
    Column {
        OutlinedTextField(
            modifier = Modifier
                .padding(
                    top = 8.dp,
                    bottom = if (errorMessage.isNotBlank()) 0.dp else 16.dp
                )
                .fillMaxWidth(), value = addFriendTextFieldValue,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            placeholder = {
                Text(text = "Enter an email")
            },
            isError = errorMessage.isNotBlank(),
            onValueChange = {
                errorMessage = ""
                addFriendTextFieldValue = it
            }, trailingIcon = {
                TriggerFutureComposable(
                    onClickAsync = {
                        if (addFriendTextFieldValue.trim().lowercase().matchesEmail())
                            onAddFriend(addFriendTextFieldValue.trim().lowercase())
                        else throw Exception("Not a valid email")
                    },
                ) { state, trigger ->
                    when (state) {
                        is TriggerFutureState.Loading -> CircularProgressIndicator()
                        else -> {
                            if (state is TriggerFutureState.Failure)
                                errorMessage = state.error.message.toString()
                            else if (state is TriggerFutureState.Success)
                                addFriendTextFieldValue = ""
                            SimpleIconButton(
                                iconResId = com.mikkelthygesen.billsplit.R.drawable.ic_add_plus,
                                onClick = trigger
                            )
                        }
                    }
                }
            }
        )
        if (errorMessage.isNotBlank())
            Text(
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
                text = errorMessage,
                style = TextStyle(color = Color.Red)
            )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    Box(modifier = Modifier.height(100.dp)) {
        _AddFriendTextField(onAddFriend = { Friend.FriendAccepted(Person()) })
    }
}