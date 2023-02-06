package com.mikkelthygesen.billsplit.domain.usecases

import com.mikkelthygesen.billsplit.data.local.database.BillSplitDb
import com.mikkelthygesen.billsplit.data.remote.auth.AuthProvider
import com.mikkelthygesen.billsplit.domain.models.Person
import com.mikkelthygesen.billsplit.domain.models.SubscriptionService
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetServiceFromLocalUseCase @Inject constructor(
    private val database: BillSplitDb,
    private val authProvider: AuthProvider
) {

    suspend fun execute(
        groupId: String,
        serviceId: String,
    ): SubscriptionService {
        // if serviceId is empty, load new service
        if (serviceId.isBlank()) {
            val groupDb = database.groupsDao().getGroup(groupId = groupId)
            return SubscriptionService(
                payer = authProvider.requireLoggedInUser,
                participants = groupDb.people.map { Person(it) }
            )
        }
        val serviceDb = database.servicesDao().getService(serviceId)
        return SubscriptionService(serviceDb)
    }
}