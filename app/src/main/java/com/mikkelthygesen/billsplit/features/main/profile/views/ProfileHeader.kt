package com.mikkelthygesen.billsplit.features.main.profile.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.ui.shadowModifier
import com.mikkelthygesen.billsplit.features.main.widgets.ProfilePictureWithUpload
import com.mikkelthygesen.billsplit.domain.models.Person
import com.mikkelthygesen.billsplit.ui.theme.listItemColor
import com.mikkelthygesen.billsplit.ui.widgets.LoadingView
import com.mikkelthygesen.billsplit.ui.widgets.TriggerFutureComposable
import com.mikkelthygesen.billsplit.ui.widgets.SimpleIconButton
import com.mikkelthygesen.billsplit.ui.widgets.TriggerFutureState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileHeader(
    user: Person,
    onUpdateUser: suspend () -> Unit,
    onError: (Throwable) -> Unit
) {
    val focusRequester = LocalFocusManager.current
    var showSaveNameOptions by rememberSaveable {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfilePictureWithUpload(user)
        Column(
            Modifier.shadowModifier(listItemColor())
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = user.nameState,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 20.sp),
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
                        containerColor = Color.Transparent,
                        textColor = MaterialTheme.colorScheme.onBackground
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
                                        tint = MaterialTheme.colorScheme.error
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
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 20.sp)
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