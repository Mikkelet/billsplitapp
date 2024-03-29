package com.mikkelthygesen.billsplit.features.main.group.group_view

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.domain.models.*
import com.mikkelthygesen.billsplit.domain.models.interfaces.Event
import com.mikkelthygesen.billsplit.features.main.group.GroupViewModel
import com.mikkelthygesen.billsplit.features.main.group.widgets.*
import com.mikkelthygesen.billsplit.sampleSharedExpenses
import com.mikkelthygesen.billsplit.tryCatchDefault
import com.mikkelthygesen.billsplit.ui.widgets.ProfilePicture
import kotlinx.coroutines.launch


@Composable
fun GroupEventsView(
    modifier: Modifier = Modifier,
    groupId: String,
    lazyListState: LazyListState = rememberLazyListState()
) {
    val viewModel: GroupViewModel = viewModel()
    val eventsFlowState = viewModel.eventsFlow().collectAsState(emptyList())
    val eventsState = eventsFlowState.value
    _ListViewExpense(
        modifier = modifier,
        events = eventsState,
        loggedInUser = viewModel.requireLoggedInUser,
        lazyListState = lazyListState
    )
}


@Composable
@SuppressLint("ComposableNaming")
private fun _ListViewExpense(
    modifier: Modifier = Modifier,
    loggedInUser: Person,
    events: List<Event>,
    lazyListState: LazyListState = rememberLazyListState()
) {
    val coroutineScope = rememberCoroutineScope()
    var focusListItemIndex by remember {
        mutableStateOf(-1)
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
            Box(modifier = Modifier.height(64.dp))
        }
        items(
            count = events.size,
            key = { events[it].id }) { index ->

            val event = events[index]
            val position: Position = getPosition(index, event, events)
            val latestIndex = isLatestIndex(index, event, events)
            val shouldShowProfilePictureLeft = event.createdBy != loggedInUser && event !is Payment
            val shouldShowProfilePictureRight = event.createdBy == loggedInUser && event !is Payment
            Row(
                Modifier.padding(vertical = 4.dp),
                Arrangement.SpaceEvenly
            ) {
                ProfilePictureBubble(
                    modifier = Modifier.align(Alignment.Bottom),
                    createdBy = event.createdBy,
                    isCreatedByUser = false,
                    show = shouldShowProfilePictureLeft,
                    isLatestIndex = latestIndex
                )
                Box(
                    modifier = Modifier
                        .weight(1f),
                ) {
                    when (event) {
                        is GroupExpense -> ListViewExpense(
                            groupExpense = event,
                            isFocused = index == focusListItemIndex,
                            position = position
                        )
                        is Payment -> ListViewPayment(payment = event)
                        is GroupExpensesChanged -> ChangesListView(
                            groupExpensesChanged = event,
                            position = position,
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
                ProfilePictureBubble(
                    modifier = Modifier.align(Alignment.Bottom),
                    createdBy = loggedInUser,
                    isCreatedByUser = true,
                    show = shouldShowProfilePictureRight,
                    isLatestIndex = latestIndex
                )
            }
        }
        item { Box(modifier = Modifier.padding(top = 80.dp)) }
    }
}

private const val pfpBubbleSize = 48
@Composable
private fun ProfilePictureBubble(
    modifier: Modifier,
    createdBy: Person,
    isCreatedByUser : Boolean,
    show: Boolean,
    isLatestIndex: Boolean
) {
    if (show) {
        if (isLatestIndex)
            ProfilePicture(
                modifier = modifier
                    .size(pfpBubbleSize.dp)
                    .let {
                        if (isCreatedByUser)
                            it.padding(start = 8.dp)
                        else it.padding(end = 8.dp)
                    },
                person = createdBy
            )
        else Box(modifier = Modifier.width(pfpBubbleSize.dp))
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

private fun isLatestIndex(index: Int, event: Event, events: List<Event>): Boolean {
    return tryCatchDefault(true) {
        events[index - 1].createdBy != event.createdBy
                || events[index - 1] is Payment
    }
}

private fun getPosition(index: Int, event: Event, events: List<Event>): Position {
    val isPrevEventByUser = tryCatchDefault(false) {
        val prevEvent = events[index - 1]
        prevEvent.createdBy == event.createdBy && prevEvent !is Payment
    }
    val isNextEventByUser = tryCatchDefault(false) {
        val nextEvent = events[index + 1]
        nextEvent.createdBy == event.createdBy && nextEvent !is Payment
    }
    return when {
        !isPrevEventByUser && isNextEventByUser -> Position.Start
        isPrevEventByUser && !isNextEventByUser -> Position.End
        isPrevEventByUser && isNextEventByUser -> Position.Middle
        else -> Position.Single
    }
}