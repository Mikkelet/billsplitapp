package com.mikkelthygesen.billsplit.ui.widgets

import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@Composable
fun CircularImageView(modifier: Modifier = Modifier, imageResId: Int) {
    Image(
        modifier = modifier
            .aspectRatio(1f)
            .clip(CircleShape),
        painter = painterResource(id = imageResId),
        contentDescription = "",
        contentScale = ContentScale.Crop
    )
}