package com.mikkelthygesen.billsplit.ui.features.group.group_view

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.models.GroupExpense
import com.mikkelthygesen.billsplit.models.GroupExpensesChanged
import com.mikkelthygesen.billsplit.models.Payment
import com.mikkelthygesen.billsplit.sampleSharedExpenses
import com.mikkelthygesen.billsplit.tryCatchDefault
import com.mikkelthygesen.billsplit.ui.features.group.GroupViewModel
import com.mikkelthygesen.billsplit.ui.features.group.widgets.ChangesListView
import com.mikkelthygesen.billsplit.ui.features.group.widgets.ListViewExpense
import com.mikkelthygesen.billsplit.ui.features.group.widgets.ListViewPayment
import com.mikkelthygesen.billsplit.ui.widgets.CircularUrlImageView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun GroupEventsView(
    modifier: Modifier = Modifier,
    viewModel: GroupViewModel = viewModel()
) {
    val eventsFlow = viewModel.eventStateFlow.collectAsState()
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
            .animateContentSize()
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
            key = { eventsState[it].id }) { index ->
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
                if (event.createdBy != viewModel.requireLoggedInUser && event !is Payment) {
                    if (latestIndex)
                        CircularUrlImageView(
                            modifier = Modifier
                                .weight(1f)
                                .align(Alignment.Bottom)
                                .padding(end = 8.dp),
                            imageUrl = event.createdBy.pfpUrlState
                        )
                    else Box(modifier = Modifier.weight(1f))
                }
                Box(
                    modifier = Modifier
                        .weight(6f)
                        .fillMaxWidth(),
                ) {
                    val isLatestMessage = index == 0
                    when (event) {
                        is GroupExpense -> ListViewExpense(
                            groupExpense = event,
                            isFocused = index == focusListItemIndex,
                            isLastMessage = isLatestMessage
                        )
                        is Payment -> ListViewPayment(payment = event)
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
                if (event.createdBy == viewModel.requireLoggedInUser && event !is Payment) {
                    if (latestIndex)
                        CircularUrlImageView(
                            modifier = Modifier
                                .weight(1f)
                                .align(Alignment.Bottom)
                                .padding(start = 8.dp),
                            imageUrl = event.createdBy.pfpUrlState
                        )
                    else Box(modifier = Modifier.weight(1f))
                }
            }
        }
        item { Box(modifier = Modifier.padding(top = 80.dp)) }
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
            contentDescription = "Add expense",
            tint = MaterialTheme.colors.onPrimary
        )
        Box(Modifier.width(8.dp))
        Text(text = "New Expense", color = MaterialTheme.colors.onPrimary)
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
        ListViewExpense(
            viewModel = GroupViewModel(),
            groupExpense = groupExpense,
            isLastMessage = true,
            isFocused = true
        )
    }
}