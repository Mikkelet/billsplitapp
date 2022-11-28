package com.mikkelthygesen.billsplit.data.network.dto

import com.mikkelthygesen.billsplit.models.Group

@kotlinx.serialization.Serializable
data class GroupDTO(
    val id: String,
    val name: String,
    val people: List<String>,
    val createdBy: String,
    val timeStamp: Long
) {
    companion object {

        fun fromGroup(group: Group) = GroupDTO(
            id = group.id,
            name = group.nameState,
            people = group.peopleState.map { it.uid },
            createdBy = group.createdBy.uid,
            timeStamp = group.timeStamp
        )
    }
}