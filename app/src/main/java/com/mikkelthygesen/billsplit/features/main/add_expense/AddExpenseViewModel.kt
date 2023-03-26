package com.mikkelthygesen.billsplit.features.main.add_expense

import androidx.lifecycle.viewModelScope
import com.mikkelthygesen.billsplit.domain.models.Group
import com.mikkelthygesen.billsplit.domain.models.GroupExpense
import com.mikkelthygesen.billsplit.domain.models.GroupExpensesChanged
import com.mikkelthygesen.billsplit.domain.usecases.AddEventUseCase
import com.mikkelthygesen.billsplit.domain.usecases.DeleteEventUseCase
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
    private val deleteEventUseCase: DeleteEventUseCase
) : BaseViewModel() {

    object ExpenseSaved : UiEvent
    object ExpenseDeleted : UiEvent
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
                updateUiState(UiState.Main)
            }
        }
    }

    fun saveExpense() {
        updateUiState(UiState.Loading)
        viewModelScope.launch {
            val response =
                runCatching {
                    if (groupExpense.id.isBlank())
                        addEventUseCase.execute(group = group, event = groupExpense)
                    else
                        addEventUseCase.execute(group = group, event = getGroupExpenseChanged())
                }
            response.fold(
                onSuccess = {
                    emitUiEvent(ExpenseSaved)
                },
                onFailure = {
                    handleError(it)
                    updateUiState(UiState.Main)
                }
            )
        }
    }

    private fun getGroupExpenseChanged(): GroupExpensesChanged {
        val original = groupExpense.original()
        return GroupExpensesChanged(
            id = "",
            createdBy = requireLoggedInUser,
            groupExpenseOriginal = original,
            groupExpenseEdited = groupExpense,
        )
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

    fun deleteExpense() {
        viewModelScope.launch {
            updateUiState(UiState.Loading)
            val response = runCatching { deleteEventUseCase(groupId = group.id, groupExpense) }
            response.foldSuccessDefault {
                emitUiEvent(ExpenseDeleted)
            }
        }
    }
}