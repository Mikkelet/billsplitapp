package com.mikkelthygesen.billsplit.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.mikkelthygesen.billsplit.Person
import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.tryCatchDefault
import com.mikkelthygesen.billsplit.tryParseToFloat


@Composable
fun PersonView(
    person: Person,
    onChangeListener: (Float) -> Unit,
    onRemoveClicked: ((Person) -> Unit)?,
    owed: String,
    enableNameChange: Boolean,
) {
    var textFieldValue by remember {
        val state = if (person.owed == 0F) "" else "${person.owed}"
        mutableStateOf(state)
    }
    var showDialog by remember {
        mutableStateOf(false)
    }
    Row {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "Person",
            modifier = Modifier.padding(8.dp)
        )
        Column {
            if (showDialog)
                ChangeNameDialog(
                    textFieldValue = person.name,
                    onConfirm = {
                        person.name = it
                        showDialog = false
                    },
                    onDismiss = { showDialog = false })
            ClickableText(
                text = AnnotatedString(person.name),
                onClick = {
                    if (enableNameChange)
                        showDialog = true
                },
                style = TextStyle(fontSize = 7f.em)
            )
            TextField(
                value = textFieldValue,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = tryParseToFloat(person, textFieldValue),
                onValueChange = {
                    textFieldValue = it
                    val changeValue = when {
                        it.isNotBlank() -> tryCatchDefault(0F) {
                            textFieldValue.toFloat()
                        }
                        else -> 0F
                    }
                    onChangeListener(changeValue)
                },
                placeholder = {
                    Text(text = "0")
                }
            )
            Row {
                if (onRemoveClicked != null)
                    Button(onClick = {
                        onRemoveClicked(person)
                    }) {
                        Text(text = "Remove")
                    }
                Text(text = "Owes $$owed")
            }
        }
    }
}

@Composable
private fun ChangeNameDialog(
    textFieldValue: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var changeNameTextFieldValue by remember {
        mutableStateOf(textFieldValue)
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Change participant's name") },
        text = {
            TextField(
                value = changeNameTextFieldValue,
                onValueChange = { changeNameTextFieldValue = it },
                isError = changeNameTextFieldValue.isBlank(),
                placeholder = {
                    Text(text = "Enter a name")
                },
                trailingIcon = {
                    IconButton(
                        onClick = { changeNameTextFieldValue = "" }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_outline_cancel_24),
                            contentDescription = "Clear text"
                        )
                    }
                }
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    if (changeNameTextFieldValue.isNotBlank())
                        onConfirm(changeNameTextFieldValue)
                }
            ) {
                Text(text = "Apply")
            }
        }
    )
}

@Preview
@Composable
fun PreviewPersonView() {
    PersonView(
        person = Person("Person 1", 100F),
        onChangeListener = {},
        onRemoveClicked = {},
        owed = "100",
        enableNameChange = true
    )
}
