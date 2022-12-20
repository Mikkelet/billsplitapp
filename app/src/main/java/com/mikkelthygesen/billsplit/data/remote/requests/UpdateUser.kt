package com.mikkelthygesen.billsplit.data.remote.requests

import com.mikkelthygesen.billsplit.data.remote.dto.PersonDTO

object UpdateUser {

    @kotlinx.serialization.Serializable
    class Request(val user: PersonDTO)
}