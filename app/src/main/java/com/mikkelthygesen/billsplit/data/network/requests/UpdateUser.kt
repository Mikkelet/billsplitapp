package com.mikkelthygesen.billsplit.data.network.requests

import com.mikkelthygesen.billsplit.data.network.dto.PersonDTO

object UpdateUser {

    @kotlinx.serialization.Serializable
    class Request(val user: PersonDTO)
}