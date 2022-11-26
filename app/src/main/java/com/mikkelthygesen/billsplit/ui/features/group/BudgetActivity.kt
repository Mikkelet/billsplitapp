package com.mikkelthygesen.billsplit.ui.features.group

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class BudgetActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GroupView()
        }
    }
}