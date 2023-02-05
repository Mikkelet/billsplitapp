package com.mikkelthygesen.billsplit.features.main.add_group

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Alignment.Companion.TopStart
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.domain.models.Group
import com.mikkelthygesen.billsplit.features.base.BaseViewModel
import com.mikkelthygesen.billsplit.features.main.add_group.wigets.FutureAddFriendDialog
import com.mikkelthygesen.billsplit.features.main.widgets.BigTopBar
import com.mikkelthygesen.billsplit.ui.shadowModifier
import com.mikkelthygesen.billsplit.sampleGroup
import com.mikkelthygesen.billsplit.ui.widgets.*

@Composable
fun AddGroupView(
    uiState: BaseViewModel.UiState,
    group: Group,
    onNext: () -> Unit,
    onBack: () -> Unit,
    onClose: () -> Unit,
    onSubmitGroup: (Group) -> Unit,
    showSubmitLoader: Boolean
) {
    val focusRequester = androidx.compose.ui.platform.LocalFocusManager.current

    val animateState by animateAlignmentAsState(
        targetAlignment =
        if (uiState is AddGroupViewModel.AddName) CenterStart else TopStart
    )

    BackHandler {
        onBack()
    }

    fun onNextClicked() {
        when (uiState) {
            is AddGroupViewModel.Ready -> onSubmitGroup(group)
            else -> onNext()
        }
    }

    Column {
        BigTopBar(
            leadingContent = {
                BackButton(Modifier.padding(8.dp)) {
                    onBack()
                }
            },
            trailingContent = {
                CloseButton(Modifier.padding(8.dp)) {
                    onClose()
                }
            }
        )
        Text(
            modifier = Modifier
                .padding(
                    top = 32.dp,
                    start = 32.dp,
                    bottom = 32.dp
                ),
            text = if (uiState is AddGroupViewModel.AddName) "Add group" else group.nameState,
            style = MaterialTheme.typography.h4
        )
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Crossfade(modifier = Modifier.align(animateState), targetState = uiState) {
                if (it is AddGroupViewModel.AddName)
                    OutlinedTextField(
                        modifier = Modifier
                            .shadowModifier(MaterialTheme.colors.background)
                            .fillMaxWidth(),
                        value = group.nameState,
                        singleLine = true,
                        onValueChange = { value -> group.nameState = value },
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
            }

            val addParticipantAlignmentState by animateAlignmentAsState(
                targetAlignment = when (uiState) {
                    AddGroupViewModel.AddName -> Center
                    else -> TopCenter
                }
            )

            Crossfade(
                modifier = Modifier.align(addParticipantAlignmentState),
                targetState = uiState
            ) {
                val showAddParticipants =
                    it is AddGroupViewModel.AddParticipants
                            || it is AddGroupViewModel.Ready
                val isEditing = it is AddGroupViewModel.AddParticipants
                if (showAddParticipants)
                    _AddGroupView(group = group, isEditing = isEditing)
            }

            if (showSubmitLoader)
                CircularProgressIndicator(
                    Modifier
                        .padding(bottom = 16.dp)
                        .align(BottomCenter)
                )
            else
                FlatButton(modifier = Modifier
                    .padding(bottom = 16.dp)
                    .align(BottomCenter),
                    icon = if (uiState is AddGroupViewModel.Ready)
                        Icons.Filled.Check else Icons.Filled.ArrowForward,
                    onClick = { onNextClicked() })
        }
    }
}

@Composable
fun animateAlignmentAsState(
    targetAlignment: Alignment,
): State<Alignment> {
    val biased = targetAlignment as BiasAlignment
    val horizontal by animateFloatAsState(biased.horizontalBias)
    val vertical by animateFloatAsState(biased.verticalBias)
    return remember {
        derivedStateOf { BiasAlignment(horizontal, vertical) }
    }
}

@SuppressLint("ComposableNaming")
@Composable
fun _AddGroupView(
    group: Group,
    isEditing: Boolean
) {

    Box(modifier = Modifier.animateContentSize()) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            if (isEditing)
                Text(
                    modifier = Modifier
                        .padding(
                            start = 32.dp, top = 16.dp, bottom = 16.dp
                        ),
                    text = "Add Participants",
                    style = MaterialTheme.typography.h6
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
                            ProfilePicture(
                                modifier = Modifier.size(48.dp),
                                person = person
                            )
                            Text(
                                modifier = Modifier.padding(start = 16.dp),
                                text = person.nameState,
                                style = MaterialTheme.typography.subtitle2
                            )
                            Box(modifier = Modifier.weight(1F))
                            if (person != group.createdBy && isEditing)
                                SimpleIconButton(
                                    Modifier.padding(end = 4.dp),
                                    iconResId = R.drawable.ic_baseline_remove_24,
                                    tint = MaterialTheme.colors.error
                                ) {
                                    group.removePerson(person)
                                }
                        }
                    }
            }
        }
        if (isEditing)
            FutureAddFriendDialog(
                modifier = Modifier
                    .padding(end = 32.dp, top = 16.dp)
                    .align(BottomEnd),
                group = group
            )
    }
}


@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    _AddGroupView(
        group = sampleGroup,
        isEditing = true
    )
}