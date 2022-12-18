package com.mikkelthygesen.billsplit.ui.features.main.add_group

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.models.Group
import com.mikkelthygesen.billsplit.sampleGroup
import com.mikkelthygesen.billsplit.ui.features.main.MainViewModel
import com.mikkelthygesen.billsplit.ui.features.main.add_group.wigets.FutureAddFriendDialog
import com.mikkelthygesen.billsplit.ui.features.main.profile.widget.shadowModifier
import com.mikkelthygesen.billsplit.ui.widgets.*

@Composable
fun AddGroupView(
    viewModel: MainViewModel = viewModel(),
) {
    RequireUserView(viewModel) {
        val group by remember {
            mutableStateOf(viewModel.getNewGroup(it))
        }

        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            Box {
                _AddGroupView(group = group)
                FutureAddFriendDialog(
                    modifier = Modifier
                        .padding(end = 32.dp, top = 16.dp)
                        .align(BottomEnd),
                    group = group
                )
            }
            ClickableFutureComposable(
                onClickAsync = { viewModel.saveGroup(group) },
                loadingComposable = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(
                                top = 64.dp,
                                bottom = 64.dp
                            )
                        )
                    }
                }
            ) { addGroup ->
                Row(
                    Modifier
                        .padding(top = 64.dp, bottom = 64.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    val enabled = group.nameState.isNotBlank()
                            && group.peopleState.isNotEmpty()
                    SimpleIconButton(
                        modifier = Modifier
                            .size(92.dp)
                            .clip(RoundedCornerShape(90.dp))
                            .background(
                                if (enabled) MaterialTheme.colors.primary else Color.Gray
                            ),
                        iconResId = R.drawable.ic_check,
                        tint = MaterialTheme.colors.onPrimary
                    ) {
                        if (enabled)
                            addGroup()
                    }
                }
            }
        }
    }
}


@SuppressLint("ComposableNaming")
@Composable
fun _AddGroupView(
    modifier: Modifier = Modifier,
    group: Group,
) {
    val focusRequester = androidx.compose.ui.platform.LocalFocusManager.current
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(top = 32.dp),
            text = "Name your group",
            style = MaterialTheme.typography.h5
        )
        OutlinedTextField(
            modifier = Modifier
                .shadowModifier(MaterialTheme.colors.background)
                .fillMaxWidth(),
            value = group.nameState,
            singleLine = true,
            onValueChange = { group.nameState = it },
            placeholder = { Text(text = ("fx. Trip to Madrid")) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { focusRequester.clearFocus() }
            ),
            colors = TextFieldDefaults.textFieldColors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                backgroundColor = MaterialTheme.colors.background.copy(alpha = .7f),
                textColor = MaterialTheme.colors.onBackground
            )
        )
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = "Add Participants",
            style = MaterialTheme.typography.h5
        )
        Column(
            modifier = Modifier
                .shadowModifier(MaterialTheme.colors.background)
                .animateContentSize()
        ) {
            if (group.peopleState.isNotEmpty())
                group.peopleState.mapIndexed { index, person ->
                    val addExtraBottomPadding = group.peopleState.size > 1
                            && index == group.peopleState.lastIndex
                    Row(
                        modifier = Modifier
                            .padding(
                                top = 8.dp,
                                bottom = if (addExtraBottomPadding) 40.dp else 8.dp
                            )
                            .fillMaxWidth(),
                        verticalAlignment = CenterVertically
                    ) {
                        CircularUrlImageView(
                            modifier = Modifier.size(48.dp),
                            imageUrl = person.pfpUrlState
                        )
                        Text(
                            modifier = Modifier.padding(start = 16.dp),
                            text = person.nameState,
                            style = MaterialTheme.typography.subtitle2
                        )
                        Box(modifier = Modifier.weight(1F))
                        if (person != group.createdBy)
                            SimpleIconButton(
                                modifier.padding(end = 4.dp),
                                iconResId = R.drawable.ic_baseline_remove_24,
                                tint = MaterialTheme.colors.error
                            ) {
                                group.removePerson(person)
                            }
                    }
                }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    _AddGroupView(
        group = sampleGroup,
    )
}