package com.mikkelthygesen.billsplit.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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

data class PersonViewFlags(
    val enableEditName: Boolean,
    val enableParticipationToggle: Boolean,
    val enableRemoval: Boolean
) {
    companion object {
        fun allDisabled() = PersonViewFlags(
            enableEditName = false,
            enableParticipationToggle = false,
            enableRemoval = false
        )

        fun participant() = PersonViewFlags(
            enableEditName = false,
            enableParticipationToggle = true,
            enableRemoval = false
        )
    }
}

@Composable
fun PersonView(
    person: Person,
    onChangeListener: (Float) -> Unit,
    onRemoveClicked: (Person) -> Unit,
    owed: String,
    flags: PersonViewFlags
) {
    var textFieldValue by remember {
        val state = if (person.owed == 0F) "" else "${person.owed}"
        mutableStateOf(state)
    }
    var showDialog by remember {
        mutableStateOf(false)
    }
    Row(modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "Person",
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
                .padding(8.dp)
        )
        Column(modifier = Modifier.fillMaxWidth()) {
            if (showDialog)
                ChangeNameDialog(
                    textFieldValue = person.name,
                    onConfirm = {
                        person.name = it
                        showDialog = false
                    },
                    onDismiss = { showDialog = false })
            Row {
                ClickableText(
                    text = AnnotatedString(person.name),
                    onClick = {
                        if (flags.enableEditName)
                            showDialog = true
                    },
                    style = TextStyle(fontSize = 7f.em)
                )
                if (flags.enableParticipationToggle)
                    Checkbox(checked = person.isParticipant, onCheckedChange = {
                        person.isParticipant = it
                    })
            }
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
                        person.isParticipant -> 0F
                        else -> 0F
                    }
                    onChangeListener(changeValue)
                },
                placeholder = {
                    Text(text = "0")
                }
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                if (flags.enableRemoval)
                    Button(onClick = {
                        onRemoveClicked(person)
                    }) {
                        Text(text = "Remove")
                    }
                Text(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    text = "Owes $owed"
                )
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

@Preview(showBackground = true)
@Composable
fun PreviewPersonView() {
    val flags = PersonViewFlags(
        enableEditName = true,
        enableParticipationToggle = true,
        enableRemoval = true
    )
    PersonView(
        person = Person("Person 1", 100F),
        onChangeListener = {},
        onRemoveClicked = {},
        owed = "100",
        flags = flags
    )
}
