package com.mikkelthygesen.billsplit.ui.features.shared_budget

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.fmt2dec
import com.mikkelthygesen.billsplit.models.GroupExpense
import com.mikkelthygesen.billsplit.models.GroupExpensesChanged
import com.mikkelthygesen.billsplit.models.Payment
import com.mikkelthygesen.billsplit.models.Person
import com.mikkelthygesen.billsplit.sampleSharedExpenses
import com.mikkelthygesen.billsplit.tryCatchDefault
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SharedBudgetView(
    viewModel: SharedBudgetViewModel = viewModel()
) {
    val flow = viewModel.shareableStateFlow().collectAsState(initial = emptyList())
    val shareablesState = flow.value.sortedBy { it.timeStamp }.reversed()
    val lazyListState = LazyListState()
    val coroutineScope = rememberCoroutineScope()
    var focusListItemIndex by remember {
        mutableStateOf(-1)
    }
    LaunchedEffect(Unit) {
        delay(5000L)
        focusListItemIndex = -1
    }
    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .fillMaxHeight()
            .fillMaxWidth(),
        reverseLayout = true,
        state = lazyListState
    ) {
        item {
            Row(Modifier.fillMaxWidth(), Arrangement.End) {
                Menu()
            }
        }
        items(
            count = shareablesState.size,
            key = { shareablesState[it].timeStamp }) { index ->
            Row(
                Modifier.padding(vertical = 4.dp),
                Arrangement.SpaceEvenly
            ) {
                val shareable = shareablesState[index]
                val latestIndex =
                    tryCatchDefault(true) {
                        shareablesState[index - 1].createdBy != shareable.createdBy
                                || shareablesState[index - 1] is Payment
                    }
                if (shareable.createdBy != viewModel.getLoggedIn() && shareable !is Payment) {
                    if (latestIndex)
                        ProfilePicture(
                            person = shareable.createdBy,
                            modifier = Modifier
                                .weight(1f)
                                .align(Alignment.Bottom)
                                .padding(end = 8.dp)
                        ) else Box(modifier = Modifier.weight(1f))
                }
                Box(
                    modifier = Modifier
                        .weight(6f)
                        .fillMaxWidth(),
                ) {
                    val isLatestMessage = index == 0
                    when (shareable) {
                        is GroupExpense -> SharedExpenseListItem(
                            groupExpense = shareable,
                            isFocused = index == focusListItemIndex,
                            isLastMessage = isLatestMessage
                        )
                        is Payment -> PaymentListView(payment = shareable)
                        is GroupExpensesChanged -> ChangesListView(
                            groupExpensesChanged = shareable,
                            isLastMessage = isLatestMessage,
                            onClickGoToExpense = { id ->
                                coroutineScope.launch {
                                    val groupExpense =
                                        shareablesState.first { it is GroupExpense && it.id == id }
                                    val indexOf = shareablesState.indexOf(groupExpense)
                                    lazyListState.animateScrollToItem(indexOf)
                                    focusListItemIndex = indexOf
                                }
                            }
                        )
                    }
                }
                if (shareable.createdBy == viewModel.getLoggedIn() && shareable !is Payment) {
                    if (latestIndex)
                        ProfilePicture(
                            person = shareable.createdBy,
                            modifier = Modifier
                                .weight(1f)
                                .align(Alignment.Bottom)
                                .padding(start = 8.dp)
                        )
                    else Box(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun ProfilePicture(person: Person, modifier: Modifier) {
    Image(
        modifier = modifier
            .aspectRatio(1f)
            .clip(CircleShape),
        painter = painterResource(id = person.pfpResId),
        contentDescription = "Person profile picture for ${person.nameState}",
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun PaymentListView(payment: Payment) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${payment.createdBy.nameState} paid $${payment.amount} to ${payment.paidTo.nameState}",
            style = TextStyle(color = Color.Gray)
        )
    }
}

@Composable
private fun ChangesListView(
    groupExpensesChanged: GroupExpensesChanged,
    isLastMessage: Boolean,
    onClickGoToExpense: (String) -> Unit
) {
    val original = groupExpensesChanged.groupExpenseOriginal
    val updated = groupExpensesChanged.groupExpenseEdited
    var expanded by remember {
        mutableStateOf(isLastMessage)
    }
    ExpandableView(
        modifier = Modifier.clickable { expanded = !expanded },
        expanded = expanded,
        iconResId = R.drawable.ic_baseline_search_24,
        onIconClick = { onClickGoToExpense(groupExpensesChanged.groupExpenseOriginal.id) }
    ) {
        Text(text = "${groupExpensesChanged.createdBy.nameState} made changes to an expense")
        if (expanded) {
            Row {
                val wasTotalChanged = original.total != updated.total
                val wasPayerChanged = original.payeeState != updated.payeeState
                val wasDescriptionChanged =
                    original.descriptionState != updated.descriptionState
                val wasParticipantsChanged =
                    original.getParticipants().size != updated.getParticipants().size
                Column(Modifier.height(intrinsicSize = IntrinsicSize.Max), Arrangement.Top) {
                    if (wasTotalChanged) Text(text = "- Total")
                    if (wasPayerChanged) Text(text = "- Payer")
                    if (wasDescriptionChanged) Text(text = "- Description")
                    if (wasParticipantsChanged) Text(text = "- Shared")
                    original.individualExpenses.mapIndexed { index, originalExpense ->
                        val updatedExpense = updated.individualExpenses[index]
                        if (originalExpense.expenseState != updatedExpense.expenseState)
                            Text(text = "- ${originalExpense.person.nameState}")
                    }
                }
                Column(Modifier.padding(horizontal = 12.dp)) {
                    if (wasTotalChanged)
                        Text(text = "$${groupExpensesChanged.groupExpenseOriginal.total.fmt2dec()} ▶ $${groupExpensesChanged.groupExpenseEdited.total.fmt2dec()}")
                    if (wasPayerChanged)
                        Text(text = "${original.payeeState.nameState} ▶ ${updated.payeeState.nameState}")
                    if (wasDescriptionChanged)
                        Text(
                            text = "\"${updated.descriptionState}\"",
                            style = TextStyle(fontStyle = FontStyle.Italic)
                        )
                    if (wasParticipantsChanged)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            ParticipantsView(list = original.getParticipants())
                            Text(text = " ▶ ")
                            ParticipantsView(list = updated.getParticipants())
                        }
                    original.individualExpenses.mapIndexed { index, originalExpense ->
                        val updatedExpense = updated.individualExpenses[index]
                        if (originalExpense.expenseState != updatedExpense.expenseState)
                            Text(text = "$${originalExpense.expenseState.fmt2dec()} ▶ $${updatedExpense.expenseState.fmt2dec()}")
                    }
                }
            }
        }
    }
}

@Composable
private fun SharedExpenseListItem(
    viewModel: SharedBudgetViewModel = viewModel(),
    groupExpense: GroupExpense,
    isFocused: Boolean,
    isLastMessage: Boolean
) {
    var expanded by remember {
        mutableStateOf(isLastMessage)
    }
    ExpandableView(
        modifier = Modifier.let {
            if (isFocused)
                it
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colors.secondary)
                    .padding(2.dp)
                    .clickable { expanded = !expanded }
            else it.clickable { expanded = !expanded }
        },
        expanded = expanded,
        iconResId = R.drawable.ic_baseline_edit_24,
        onIconClick = { viewModel.editSharedExpense(groupExpense) }
    ) {
        if (groupExpense.descriptionState.isNotBlank())
            Text(
                text = "\"${groupExpense.descriptionState}\"",
                style = TextStyle(fontSize = 18.sp, fontStyle = FontStyle.Italic)
            )
        else Text(text = "${groupExpense.createdBy.nameState} added a new expense!")
        Box(modifier = Modifier.height(8.dp))
        if (!expanded)
            Text(
                text = "$${groupExpense.total.fmt2dec()}",
                style = TextStyle(fontStyle = FontStyle.Italic, fontSize = 20.sp)
            )
        else {
            Text(text = "$${groupExpense.total.fmt2dec()} was paid by ${groupExpense.payeeState.nameState}")
            Box(modifier = Modifier.height(8.dp))
            Row {
                Column {
                    if (groupExpense.sharedExpenseState > 0F)
                        Text(text = "Shared")
                    groupExpense.individualExpenses.map {
                        if (it.expenseState > 0F) {
                            Text(text = it.person.nameState)
                        }
                    }
                }
                Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                    if (groupExpense.sharedExpenseState > 0f)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "$${groupExpense.sharedExpenseState.fmt2dec()} / ")
                            val participants =
                                groupExpense.individualExpenses
                                    .filter { it.isParticipantState }
                                    .map { it.person }
                            ParticipantsView(list = participants)
                        }
                    groupExpense.individualExpenses.map {
                        if (it.expenseState > 0F) {
                            Text(text = "$${it.expenseState.fmt2dec()}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Menu(
    viewModel: SharedBudgetViewModel = viewModel()
) {
    Row(
        Modifier
            .padding(bottom = 24.dp, top = 8.dp)
            .wrapContentWidth()
            .animateContentSize(
                animationSpec = tween<IntSize>(
                    durationMillis = 2000,
                    easing = LinearEasing
                )
            )
            .clip(CircleShape)
            .background(MaterialTheme.colors.primary)
            .clickable { viewModel.addExpense() }
            .padding(vertical = 12.dp, horizontal = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_add_plus),
            contentDescription = "Add expense"
        )
        Box(Modifier.width(8.dp))
        Text(text = "New Expense")
    }
}

@Composable
private fun ParticipantsView(list: List<Person>) {
    Row(Modifier.height(20.dp)) {
        list.map {
            ProfilePicture(person = it, modifier = Modifier.padding(horizontal = 2.dp))
        }
    }
}

@Composable
private fun ExpandableView(
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    onIconClick: () -> Unit,
    iconResId: Int,
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier
            .animateContentSize()
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colors.primary)
            .padding(vertical = 12.dp, horizontal = 8.dp),
        Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(end = 16.dp),
            horizontalAlignment = if (!expanded) Alignment.CenterHorizontally else Alignment.Start
        ) {
            content()
        }
        if (expanded)
            IconButton(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .wrapContentWidth()
                    .fillMaxHeight(),
                onClick = onIconClick,
            ) {
                Icon(
                    painter = painterResource(id = iconResId),
                    contentDescription = "Edit expense",
                    tint = MaterialTheme.colors.secondary
                )
            }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewMenu() {
    Menu()
}

@Preview(showBackground = true)
@Composable
private fun PreviewSharedExpenseListItem() {
    val groupExpense = sampleSharedExpenses.first()
    Box(
        modifier = Modifier
            .height(200.dp)
            .padding(20.dp)
    ) {
        SharedExpenseListItem(
            viewModel = SharedBudgetViewModel(),
            groupExpense = groupExpense,
            isLastMessage = true,
            isFocused = true
        )
    }
}