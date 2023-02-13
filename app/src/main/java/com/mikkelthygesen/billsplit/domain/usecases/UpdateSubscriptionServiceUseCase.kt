package com.mikkelthygesen.billsplit.domain.usecases

import com.mikkelthygesen.billsplit.data.local.database.BillSplitDb
import com.mikkelthygesen.billsplit.data.local.database.model.SubscriptionServiceDb
import com.mikkelthygesen.billsplit.data.remote.ServerApiImpl
import com.mikkelthygesen.billsplit.domain.models.SubscriptionService
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class UpdateSubscriptionServiceUseCase @Inject constructor(
    private val serverApiImpl: ServerApiImpl,
    private val database: BillSplitDb,
) {

    suspend operator fun invoke(groupId: String, service: SubscriptionService) {
        serverApiImpl.updateSubscriptionService(groupId, service)
        database.servicesDao().insert(SubscriptionServiceDb(groupId, service))
    }
}