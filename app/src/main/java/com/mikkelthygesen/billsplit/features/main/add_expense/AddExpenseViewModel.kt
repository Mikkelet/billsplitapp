package com.mikkelthygesen.billsplit.features.main.add_expense

import androidx.lifecycle.viewModelScope
import com.mikkelthygesen.billsplit.DebtCalculator
import com.mikkelthygesen.billsplit.domain.models.Group
import com.mikkelthygesen.billsplit.domain.models.GroupExpense
import com.mikkelthygesen.billsplit.domain.models.Payment
import com.mikkelthygesen.billsplit.domain.models.interfaces.Event
import com.mikkelthygesen.billsplit.domain.usecases.AddEventUseCase
import com.mikkelthygesen.billsplit.domain.usecases.GetEventsFromLocalUseCase
import com.mikkelthygesen.billsplit.domain.usecases.GetExpenseFromLocalUseCase
import com.mikkelthygesen.billsplit.domain.usecases.GetGroupUseCase
import com.mikkelthygesen.billsplit.features.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddExpenseViewModel @Inject constructor(
    private val getExpenseFromLocalUseCase: GetExpenseFromLocalUseCase,
    private val getGroupUseCase: GetGroupUseCase,
    private val addEventUseCase: AddEventUseCase,
    private val getEventsFromLocalUseCase: GetEventsFromLocalUseCase,
) : BaseViewModel() {

    object ExpenseLoaded : UiState
    object ExpenseSaved : UiEvent
    object ConfirmDialog : DialogState

    override val _mutableUiStateFlow: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    lateinit var groupExpense: GroupExpense
    lateinit var group: Group

    fun loadExpense(groupId: String, expenseId: String) {
        if (this::groupExpense.isInitialized) return
        viewModelScope.launch {
            val response =
                runCatching {
                    val group = getGroupUseCase.execute(groupId, false)
                    val expense = getExpenseFromLocalUseCase.execute(
                        expenseId = expenseId,
                        groupId = groupId
                    )
                    expense to group
                }
            response.foldSuccess {
                groupExpense = it.first
                group = it.second
                updateUiState(ExpenseLoaded)
            }
        }
    }

    fun saveExpense() {
        updateUiState(UiState.Loading)
        viewModelScope.launch {
            val response =
                runCatching {
                    group.debtsState = getCalculator(groupExpense).calculateEffectiveDebtForGroup()
                    addEventUseCase.execute(group = group, event = groupExpense)
                }
            response.fold(
                onSuccess = {
                    emitUiEvent(ExpenseSaved)
                },
                onFailure = {
                    handleError(it)
                    updateUiState(ExpenseLoaded)
                }
            )
        }
    }

    private suspend fun getCalculator(withEvent: Event? = null): DebtCalculator {
        val events = getEventsFromLocalUseCase.execute(group.id)
        val payments: List<Payment> = events.filterIsInstance<Payment>().let {
            if (withEvent is Payment) it.plus(withEvent) else it
        }
        val groupExpenses: List<GroupExpense> = events.filterIsInstance<GroupExpense>().let {
            if (withEvent is GroupExpense) it.plus(withEvent) else it
        }
        return DebtCalculator(group.peopleState, groupExpenses, payments)
    }

    fun handleBack(): Boolean {
        return when {
            groupExpense.isChanged() -> {
                showDialog(ConfirmDialog)
                true
            }
            else -> false
        }
    }
}