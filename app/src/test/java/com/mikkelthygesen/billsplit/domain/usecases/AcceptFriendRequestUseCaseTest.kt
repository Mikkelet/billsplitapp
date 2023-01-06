package com.mikkelthygesen.billsplit.domain.usecases

import com.mikkelthygesen.billsplit.data.local.database.BillSplitDb
import com.mikkelthygesen.billsplit.data.local.database.model.FriendDb
import com.mikkelthygesen.billsplit.data.remote.ServerApiImpl
import com.mikkelthygesen.billsplit.data.remote.dto.FriendDTO
import com.mikkelthygesen.billsplit.data.remote.dto.FriendStatusDTO
import com.mikkelthygesen.billsplit.data.remote.dto.PersonDTO
import com.mikkelthygesen.billsplit.domain.models.Friend
import com.mikkelthygesen.billsplit.domain.models.Person
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

internal class AcceptFriendRequestUseCaseTest {

    private val serverApiImpl: ServerApiImpl = mockk()
    private val database: BillSplitDb = mockk()
    private val acceptFriendRequestUseCase = AcceptFriendRequestUseCase(serverApiImpl, database)

    @Before
    fun setup() {
        coEvery { database.friendsDao().insert(any() as FriendDb) } returns Unit
    }

    @Test
    fun `sent request to friend`() {
        val returnResult =
            FriendDTO("", userId, FriendStatusDTO.RequestSent, friend)
        val expectedResponse = Friend.FriendRequestSent(Person(friendId))

        runBlocking {
            coEvery { serverApiImpl.acceptFriendRequest(friendId) } returns returnResult
            val response = acceptFriendRequestUseCase.execute(Person(friendId))

            assert(response == expectedResponse)
        }
    }

    @Test
    fun `accept friend request`() {
        val returnResult =
            FriendDTO("", friendId, FriendStatusDTO.RequestAccepted, PersonDTO(friendId, "", ""))

        val expectedResponse = Friend.FriendAccepted(Person(friendId))

        runBlocking {
            coEvery { serverApiImpl.acceptFriendRequest(friendId) } returns returnResult
            val response = acceptFriendRequestUseCase.execute(Person(friendId))

            assert(response == expectedResponse)
        }
    }

    companion object {
        private const val userId = "uid1"

        private const val friendId = "uid2"
        private val friend = PersonDTO(
            friendId,
            "",
            ""
        )

    }
}