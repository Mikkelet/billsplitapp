package com.mikkelthygesen.billsplit.features.group.group_view

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.features.group.GroupViewModel
import com.mikkelthygesen.billsplit.features.group.group_view.widgets.Menu
import com.mikkelthygesen.billsplit.features.group.widgets.ChangesListView
import com.mikkelthygesen.billsplit.features.group.widgets.ListViewExpense
import com.mikkelthygesen.billsplit.features.group.widgets.ListViewPayment
import com.mikkelthygesen.billsplit.models.GroupExpense
import com.mikkelthygesen.billsplit.models.GroupExpensesChanged
import com.mikkelthygesen.billsplit.models.Payment
import com.mikkelthygesen.billsplit.models.Person
import com.mikkelthygesen.billsplit.models.interfaces.Event
import com.mikkelthygesen.billsplit.sampleSharedExpenses
import com.mikkelthygesen.billsplit.tryCatchDefault
import com.mikkelthygesen.billsplit.ui.widgets.ProfilePicture
import com.mikkelthygesen.billsplit.ui.widgets.RequireUserView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun GroupEventsView(
    modifier: Modifier = Modifier,
    viewModel: GroupViewModel = viewModel()
) {
    val eventsFlow = viewModel.eventStateFlow.collectAsState()

    RequireUserView(baseViewModel = viewModel) { user ->
        val eventsState = eventsFlow.value
            .sortedBy { event -> event.timeStamp }
            .reversed()
        _ListViewExpense(
            modifier = modifier,
            events = eventsState,
            loggedInUser = user,
        )
    }
}


@Composable
@SuppressLint("ComposableNaming")
private fun _ListViewExpense(
    modifier: Modifier = Modifier,
    loggedInUser: Person,
    events: List<Event>
) {
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
            count = events.size,
            key = { events[it].id }) { index ->
            Row(
                Modifier.padding(vertical = 4.dp),
                Arrangement.SpaceEvenly
            ) {
                val event = events[index]
                val latestIndex =
                    tryCatchDefault(true) {
                        events[index - 1].createdBy != event.createdBy
                                || events[index - 1] is Payment
                    }
                if (event.createdBy != loggedInUser && event !is Payment) {
                    if (latestIndex)
                        ProfilePicture(
                            modifier = Modifier
                                .weight(1f)
                                .align(Alignment.Bottom)
                                .padding(end = 8.dp),
                            person = event.createdBy
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
                                        events.first { it is GroupExpense && it.id == id }
                                    val indexOf = events.indexOf(groupExpense)
                                    lazyListState.animateScrollToItem(indexOf)
                                    focusListItemIndex = indexOf
                                }
                            }
                        )
                    }
                }
                if (event.createdBy == loggedInUser && event !is Payment) {
                    if (latestIndex)
                        ProfilePicture(
                            modifier = Modifier
                                .weight(1f)
                                .align(Alignment.Bottom)
                                .padding(start = 8.dp),
                            person = event.createdBy
                        )
                    else Box(modifier = Modifier.weight(1f))
                }
            }
        }
        item { Box(modifier = Modifier.padding(top = 80.dp)) }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSharedExpenseListItem() {
    Box(
        modifier = Modifier
            .height(200.dp)
            .padding(20.dp)
    ) {
        _ListViewExpense(
            modifier = Modifier,
            loggedInUser = Person("Mikkel"),
            events = sampleSharedExpenses
        )
    }
}