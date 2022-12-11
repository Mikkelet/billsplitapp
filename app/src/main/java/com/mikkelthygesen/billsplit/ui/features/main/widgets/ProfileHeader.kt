package com.mikkelthygesen.billsplit.ui.features.main.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.models.Person
import com.mikkelthygesen.billsplit.ui.features.main.profile.widget.shadowModifier
import com.mikkelthygesen.billsplit.ui.widgets.CircularImageView
import com.mikkelthygesen.billsplit.ui.widgets.ClickableFutureComposable
import com.mikkelthygesen.billsplit.ui.widgets.IconButton


@Composable
fun ProfileHeader(
    user: Person,
    onUpdateUser: suspend () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularImageView(
            modifier = Modifier
                .padding(top = 32.dp)
                .size(100.dp),
            imageResId = user.pfpResId
        )
        val focusRequester = LocalFocusManager.current
        var showSaveNameButton by remember {
            mutableStateOf(false)
        }
        Column(
            shadowModifier(MaterialTheme.colors.background)
        ) {
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = user.nameState,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.body1.copy(fontSize = 20.sp),
                    onValueChange = {
                        user.nameState = it
                        showSaveNameButton = user.isNameChanged()
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        backgroundColor = Color.Transparent,
                        textColor = MaterialTheme.colors.onBackground
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusRequester.clearFocus()
                        }
                    )
                )
                if (showSaveNameButton)
                    ClickableFutureComposable(
                        asyncCallback = { onUpdateUser() },
                        onSuccess = {
                            user.saveChanges()
                            showSaveNameButton = false
                        },
                    ) {
                        IconButton(iconResId = R.drawable.ic_check) {
                            it.invoke()
                        }
                    }
            }
            Divider(Modifier.padding(vertical = 16.dp, horizontal = 64.dp))
            Text(
                modifier = Modifier.padding(16.dp),
                text = "test email",
                style = MaterialTheme.typography.body1.copy(fontSize = 20.sp)
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    ProfileHeader(user = Person("pokaspd123123psodak", name = "Catra"), onUpdateUser = {})
}