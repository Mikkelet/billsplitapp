package com.mikkelthygesen.billsplit.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mikkelthygesen.billsplit.Person
import com.mikkelthygesen.billsplit.R


@Composable
fun PersonView(
    person: Person,
    onChangeListener: (Float) -> Unit,
    onRemoveClicked: (() -> Unit)?,
    owed: String
) {
    var textFieldValue by remember {
        val state = if (person.owed == 0F) "" else "${person.owed}"
        mutableStateOf(state)
    }
    Row {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "Person",
            modifier = Modifier.padding(8.dp)
        )
        Column {
            Text(text = person.name)
            TextField(
                value = textFieldValue,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = tryParseToFloat(person, textFieldValue),
                onValueChange = {
                    textFieldValue = it
                    val changeValue = when {
                        it.isNotBlank() -> try {
                            textFieldValue.toFloat()
                        } catch (e: Exception) {
                            println("qqq error=$e")
                            0F
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
                    Button(onClick = onRemoveClicked) {
                        Text(text = "Remove")
                    }
                Text(text = "Owes $$owed")
            }
        }
    }
}

private fun tryParseToFloat(person: Person, value: String) = try {
    person.owed = value.toFloat()
    false
} catch (e: Exception) {
    true
}