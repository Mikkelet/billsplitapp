package com.mikkelthygesen.billsplit.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikkelthygesen.billsplit.domain.models.Person

@Composable
fun AutoProfilePic(modifier: Modifier = Modifier, user: Person) {
    val userName = user.nameState
    val displayName = if (userName.trim().contains(" ")) {
        val split = userName.split(" ").filter { it.isNotBlank() }
        val initials = split.map { it.first().toString() }
            .reduce { acc, c -> "${acc}$c" }
        initials.substring(0..1).uppercase()
    } else userName.first().toString().uppercase()

    val userColor = Color(user.uid.hashCode()).copy(alpha = 1f)
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(userColor)
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = displayName,
            style = TextStyle(
                fontSize = 30.sp,
                color = if (userColor.luminance() > 0.5)
                    Color.Black else Color.White
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFfbddcc)
@Preview(showBackground = true, backgroundColor = 0xFF123432)
@Composable
private fun Preview() {
    Box(Modifier.size(100.dp)) {
        AutoProfilePic(user = Person(name = "M e", uid = "13112dsfad312"))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFfbddcc)
@Preview(showBackground = true, backgroundColor = 0xFF123432)
@Composable
private fun Preview2() {
    Box(Modifier.size(100.dp)) {
        AutoProfilePic(user = Person(name = "mIkkel", uid = "13112dasdasd312"))
    }
}