package com.mikkelthygesen.billsplit.domain.usecases

import com.mikkelthygesen.billsplit.data.local.database.BillSplitDb
import com.mikkelthygesen.billsplit.data.local.database.model.SubscriptionServiceDb
import com.mikkelthygesen.billsplit.data.remote.ServerApiImpl
import com.mikkelthygesen.billsplit.domain.models.SubscriptionService
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject
@ViewModelScoped
class AddSubscriptionServiceUseCase @Inject constructor(
    private val serverApiImpl: ServerApiImpl,
    private val database: BillSplitDb,
) {

    suspend fun execute(groupId: String, service: SubscriptionService): SubscriptionService {
        val dto = serverApiImpl.addSubscriptionService(groupId, service)
        database.servicesDao().insert(SubscriptionServiceDb(groupId, dto))
        return SubscriptionService(dto)
    }
}