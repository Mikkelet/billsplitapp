package com.mikkelthygesen.billsplit.domain.usecases

import com.mikkelthygesen.billsplit.data.remote.ServerApiImpl
import com.mikkelthygesen.billsplit.domain.models.SubscriptionService
import javax.inject.Inject

class AddSubscriptionServiceUseCase @Inject constructor(
    private val serverApiImpl: ServerApiImpl
) {

    suspend fun execute(groupId: String, service: SubscriptionService): SubscriptionService {
        val dto = serverApiImpl.addSubscriptionService(groupId, service)
        return SubscriptionService(dto)
    }
}