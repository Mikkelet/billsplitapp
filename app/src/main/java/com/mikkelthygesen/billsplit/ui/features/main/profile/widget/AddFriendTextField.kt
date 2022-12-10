package com.mikkelthygesen.billsplit.ui.features.main.profile.widget

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.models.Friend
import com.mikkelthygesen.billsplit.models.Person
import com.mikkelthygesen.billsplit.ui.features.main.MainViewModel
import com.mikkelthygesen.billsplit.ui.widgets.ClickableFutureComposable
import com.mikkelthygesen.billsplit.ui.widgets.IconButton

@Composable
fun AddFriendEmailTextField(
    viewModel: MainViewModel = viewModel(),
    onFriendAdded: (Friend) -> Unit
) {
    _AddFriendTextField {
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

    OutlinedTextField(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(), value = addFriendTextFieldValue,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        placeholder = {
            Text(text = "Enter an email")
        },
        onValueChange = {
            addFriendTextFieldValue = it
        }, trailingIcon = {
            ClickableFutureComposable(
                asyncCallback = {
                    onAddFriend(addFriendTextFieldValue)
                },
                onSuccess = {
                    addFriendTextFieldValue = ""
                }
            ) {
                IconButton(
                    iconResId = com.mikkelthygesen.billsplit.R.drawable.ic_money,
                    onClick = it
                )
            }
        }
    )
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    Box(modifier = Modifier.height(100.dp)) {
        _AddFriendTextField(onAddFriend = { Friend.FriendAccepted(Person()) })
    }
}