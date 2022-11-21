package com.mikkelthygesen.billsplit.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mikkelthygesen.billsplit.models.interfaces.IShareable

data class Person(
    val pId: String,
    private var name: String
) : IShareable {
    override val timeStamp: Long = System.currentTimeMillis()

    var nameState by mutableStateOf(name)

    override fun equals(other: Any?): Boolean {
        return if (other is Person)
            this.pId == other.pId
        else false
    }

    override fun hashCode(): Int {
        return pId.hashCode()
    }
}