package com.mikkelthygesen.billsplit.domain.usecases

import com.mikkelthygesen.billsplit.data.local.database.BillSplitDb
import com.mikkelthygesen.billsplit.domain.models.SubscriptionService
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ViewModelScoped
class ObserveLocalServicesUseCase @Inject constructor(
    private val billSplitDb: BillSplitDb
) {

    fun observe(groupId: String): Flow<List<SubscriptionService>> {
        return billSplitDb.servicesDao().getServicesFlow(groupId)
            .map { services -> services.map { SubscriptionService(it) } }
    }
}