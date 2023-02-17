package com.mikkelthygesen.billsplit.features.main.group.widgets

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes


enum class Position {
    Start,
    Middle,
    End,
    Single;

    fun getShape(shapes: Shapes): RoundedCornerShape = when (this) {
        End -> RoundedCornerShape(
            topEnd = shapes.extraLarge.topEnd,
            topStart = shapes.extraLarge.topStart,
            bottomEnd = shapes.small.bottomEnd,
            bottomStart = shapes.small.bottomStart
        )
        Start -> RoundedCornerShape(
            topEnd = shapes.small.topEnd,
            topStart = shapes.small.topStart,
            bottomEnd = shapes.extraLarge.bottomEnd,
            bottomStart = shapes.extraLarge.bottomStart
        )
        Middle -> RoundedCornerShape(
            topEnd = shapes.small.topEnd,
            topStart = shapes.small.topStart,
            bottomEnd = shapes.small.bottomEnd,
            bottomStart = shapes.small.bottomStart
        )
        Single -> RoundedCornerShape(
            topEnd = shapes.extraLarge.topEnd,
            topStart = shapes.extraLarge.topStart,
            bottomEnd = shapes.extraLarge.bottomEnd,
            bottomStart = shapes.extraLarge.bottomStart
        )
    }
}

