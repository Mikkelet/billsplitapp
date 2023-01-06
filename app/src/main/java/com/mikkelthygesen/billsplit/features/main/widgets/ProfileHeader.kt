package com.mikkelthygesen.billsplit.features.main.widgets

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
import com.mikkelthygesen.billsplit.features.main.profile.widget.shadowModifier
import com.mikkelthygesen.billsplit.features.main.widgets.widgets.ProfilePictureWithUpload
import com.mikkelthygesen.billsplit.domain.models.Person
import com.mikkelthygesen.billsplit.ui.theme.listItemColor
import com.mikkelthygesen.billsplit.ui.widgets.LoadingView
import com.mikkelthygesen.billsplit.ui.widgets.TriggerFutureComposable
import com.mikkelthygesen.billsplit.ui.widgets.SimpleIconButton
import com.mikkelthygesen.billsplit.ui.widgets.TriggerFutureState


@Composable
fun ProfileHeader(
    user: Person,
    onUpdateUser: suspend () -> Unit,
    onError: (Throwable) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ProfilePictureWithUpload(user)
        val focusRequester = LocalFocusManager.current
        var showSaveNameOptions by remember {
            mutableStateOf(false)
        }
        Column(
            Modifier.shadowModifier(MaterialTheme.colors.listItemColor())
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = user.nameState,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.body1.copy(fontSize = 20.sp),
                    onValueChange = {
                        user.nameState = it
                        showSaveNameOptions = user.isNameChanged()
                    },
                    placeholder = {
                        Text(text = "Click here to change your name :)")
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
                if (showSaveNameOptions)
                    TriggerFutureComposable(
                        onClickAsync = {
                            onUpdateUser()
                            focusRequester.clearFocus()
                        }
                    ) { state, updateUser ->
                        when (state) {
                            is TriggerFutureState.Loading -> LoadingView()
                            else -> {
                                if (state is TriggerFutureState.Success) {
                                    user.saveChanges()
                                    focusRequester.clearFocus()
                                    showSaveNameOptions = false
                                } else if (state is TriggerFutureState.Failure)
                                    onError(state.error)
                                Row {
                                    SimpleIconButton(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .padding(end = 4.dp),
                                        iconResId = R.drawable.ic_check,
                                        onClick = updateUser
                                    )
                                    SimpleIconButton(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .padding(end = 4.dp),
                                        iconResId = R.drawable.ic_outline_cancel_24,
                                        tint = MaterialTheme.colors.error
                                    ) {
                                        user.resetState()
                                        focusRequester.clearFocus()
                                        showSaveNameOptions = false
                                    }
                                }
                            }
                        }
                    }
            }
            Divider(Modifier.padding(vertical = 16.dp, horizontal = 64.dp))
            Text(
                modifier = Modifier.padding(16.dp),
                text = user.email,
                style = MaterialTheme.typography.body1.copy(fontSize = 20.sp)
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    ProfileHeader(
        user = Person("pokaspd123123psodak", name = "Catra"),
        onUpdateUser = {},
        onError = {})
}