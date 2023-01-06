package com.mikkelthygesen.billsplit.domain.usecases

    import com.mikkelthygesen.billsplit.data.local.database.BillSplitDb
import com.mikkelthygesen.billsplit.data.local.database.model.GroupDb
import com.mikkelthygesen.billsplit.data.remote.ServerApi
import com.mikkelthygesen.billsplit.data.remote.ServerApiImpl
import com.mikkelthygesen.billsplit.data.remote.dto.EventDTO
import com.mikkelthygesen.billsplit.data.remote.dto.GroupDTO
import com.mikkelthygesen.billsplit.data.remote.dto.PersonDTO
import com.mikkelthygesen.billsplit.data.remote.requests.AddEvent
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

internal class AddEventUseCaseTest {

    private val serverApi: ServerApi = mockk()
    private val serverApiImpl: ServerApiImpl = ServerApiImpl(serverApi)
    private val database: BillSplitDb = mockk()
    private val addEventUseCase = AddEventUseCase(database, serverApiImpl)

    @Before
    fun setup() {
        coEvery { database.groupsDao().insert(any() as GroupDb) } returns Unit
    }

    @Test
    fun `test add event`() {
        val personDTO = PersonDTO("uid1", "user 1", "")
        val paidToDTO = PersonDTO("uid2", "user 2", "")
        val groupDto =
            GroupDTO("gid", "my_group", emptyList(), PersonDTO("", "", ""), 1000L, emptyList())
        val eventDTO = EventDTO.PaymentDTO("paymentId", personDTO, 100L, paidToDTO, 100F)

        val request = AddEvent.Request(groupDto.id, eventDTO, emptyList())

        runBlocking {
            coEvery { serverApi.addEvent(request) } returns AddEvent.Response(eventDTO)

            val response = addEventUseCase.execute(groupDto.toGroup(), eventDTO.toEvent())

            assert(response == eventDTO.toEvent())
        }
    }
}