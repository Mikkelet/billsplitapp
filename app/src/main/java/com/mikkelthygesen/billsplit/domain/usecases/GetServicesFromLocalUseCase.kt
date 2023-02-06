package com.mikkelthygesen.billsplit.domain.usecases

import com.mikkelthygesen.billsplit.data.local.database.BillSplitDb
import com.mikkelthygesen.billsplit.domain.models.SubscriptionService
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetServicesFromLocalUseCase @Inject constructor(
    private val database: BillSplitDb,
) {

    suspend fun execute(
        groupId: String,
    ): List<SubscriptionService> {
        val servicesDb = database.servicesDao().getServices(groupId)
        return servicesDb.map { SubscriptionService(it) }
    }
}