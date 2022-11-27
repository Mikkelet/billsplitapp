package com.mikkelthygesen.billsplit.ui.widgets.dialogs

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.ui.features.group.GroupViewModel


@Composable
private fun AddPersonDialog(
    viewModel: GroupViewModel = viewModel()
) {
    var nameTextFieldValue by remember {
        mutableStateOf("")
    }
    AlertDialog(onDismissRequest = { viewModel.dismissDialog() },
        title = { Text(text = "Enter new name") },
        text = {
            TextField(value = nameTextFieldValue,
                onValueChange = { nameTextFieldValue = it },
                isError = nameTextFieldValue.isBlank(),
                placeholder = {
                    Text(text = "Enter a name")
                },
                trailingIcon = {
                    IconButton(onClick = { nameTextFieldValue = "" }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_outline_cancel_24),
                            contentDescription = "Clear text"
                        )
                    }
                })
        },
        confirmButton = {
            Button(onClick = {
                if (nameTextFieldValue.isNotBlank()) {
                    viewModel.addPerson(nameTextFieldValue)
                    viewModel.dismissDialog()
                }
            }) {
                Text(text = "Add")
            }
        })
}