package com.mikkelthygesen.billsplit.ui.features.group.group

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.*
import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.base.BaseViewModel
import com.mikkelthygesen.billsplit.models.GroupExpense
import com.mikkelthygesen.billsplit.models.GroupExpensesChanged
import com.mikkelthygesen.billsplit.models.Payment
import com.mikkelthygesen.billsplit.models.Person
import com.mikkelthygesen.billsplit.ui.features.group.GroupViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun GroupEventsView(
    modifier: Modifier = Modifier,
    viewModel: GroupViewModel = viewModel()
) {
    val eventsFlow = viewModel.eventStateFlow.collectAsState(initial = emptyList())
    val eventsState = eventsFlow.value.sortedBy { it.timeStamp }.reversed()
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var focusListItemIndex by remember {
        mutableStateOf(-1)
    }
    LaunchedEffect(Unit) {
        delay(5000L)
        focusListItemIndex = -1
    }
    LazyColumn(
        modifier = modifier
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
            count = eventsState.size,
            key = { eventsState[it].timeStamp }) { index ->
            Row(
                Modifier.padding(vertical = 4.dp),
                Arrangement.SpaceEvenly
            ) {
                val event = eventsState[index]
                val latestIndex =
                    tryCatchDefault(true) {
                        eventsState[index - 1].createdBy != event.createdBy
                                || eventsState[index - 1] is Payment
                    }
                if (event.createdBy != viewModel.getLoggedIn() && event !is Payment) {
                    if (latestIndex)
                        ProfilePicture(
                            person = event.createdBy,
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
                    when (event) {
                        is GroupExpense -> SharedExpenseListItem(
                            groupExpense = event,
                            isFocused = index == focusListItemIndex,
                            isLastMessage = isLatestMessage
                        )
                        is Payment -> PaymentListView(payment = event)
                        is GroupExpensesChanged -> ChangesListView(
                            groupExpensesChanged = event,
                            isLastMessage = isLatestMessage,
                            onClickGoToExpense = { id ->
                                coroutineScope.launch {
                                    val groupExpense =
                                        eventsState.first { it is GroupExpense && it.id == id }
                                    val indexOf = eventsState.indexOf(groupExpense)
                                    lazyListState.animateScrollToItem(indexOf)
                                    focusListItemIndex = indexOf
                                }
                            }
                        )
                    }
                }
                if (event.createdBy == viewModel.getLoggedIn() && event !is Payment) {
                    if (latestIndex)
                        ProfilePicture(
                            person = event.createdBy,
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
            text = "${payment.createdBy.nameState} paid $${payment.amount.fmt2dec()} to ${payment.paidTo.nameState}",
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
                val wasParticipantsChanged = original.getParticipants() != updated.getParticipants()
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
    viewModel: GroupViewModel = viewModel(),
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
    viewModel: GroupViewModel = viewModel()
) {
    Row(
        Modifier
            .padding(bottom = 24.dp, top = 8.dp)
            .wrapContentWidth()
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
            viewModel = GroupViewModel(),
            groupExpense = groupExpense,
            isLastMessage = true,
            isFocused = true
        )
    }
}