package com.mikkelthygesen.billsplit.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mikkelthygesen.billsplit.R
import java.util.UUID

data class Person(
    val uid: String = "",
    private var name: String = "",
    val pfpResId:Int = R.drawable.catra_pfp,
) {
    var nameState by mutableStateOf(name)
}