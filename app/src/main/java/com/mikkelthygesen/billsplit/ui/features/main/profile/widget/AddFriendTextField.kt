package com.mikkelthygesen.billsplit.ui.features.main.profile.widget

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.models.Friend
import com.mikkelthygesen.billsplit.models.Person
import com.mikkelthygesen.billsplit.ui.features.main.MainViewModel
import com.mikkelthygesen.billsplit.ui.widgets.ClickableFutureComposable
import com.mikkelthygesen.billsplit.ui.widgets.IconButton
import io.ktor.utils.io.core.*
import kotlinx.coroutines.delay

@Composable
fun AddFriendEmailTextField(
    viewModel: MainViewModel = viewModel(),
    onFriendAdded: (Friend) -> Unit
) {
    _AddFriendTextField {
        delay(2000L)
        val friend = viewModel.addFriend(it.trim().lowercase())
        onFriendAdded(friend)
        friend
    }
}

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
                ClickableFutureComposable(
                    asyncCallback = { onAddFriend(addFriendTextFieldValue) },
                    loadingComposable = { CircularProgressIndicator() },
                    onSuccess = { addFriendTextFieldValue = "" },
                    onError = { errorMessage = it.message.toString() }
                ) {
                    IconButton(
                        iconResId = com.mikkelthygesen.billsplit.R.drawable.ic_add_plus,
                        onClick = it
                    )
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