package com.mikkelthygesen.billsplit.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.mikkelthygesen.billsplit.R

@Composable
fun CircularImageView(modifier: Modifier = Modifier,  imageResId: Int) {
    Image(
        modifier = modifier
            .aspectRatio(1f)
            .clip(CircleShape),
        painter = painterResource(id = imageResId),
        contentDescription = "",
        contentScale = ContentScale.Crop,
    )
}

@Composable
fun CircularUrlImageView(modifier: Modifier = Modifier,  imageUrl: String) {
    Image(
        modifier = modifier
            .aspectRatio(1f)
            .clip(CircleShape),
        painter = rememberAsyncImagePainter(
            model = imageUrl.ifBlank { null },
            fallback = painterResource(id = R.drawable.ic_baseline_person_24),
            placeholder = painterResource(id = R.drawable.ic_baseline_person_24),
        ),
        contentDescription = "",
        contentScale = ContentScale.Crop,
    )
}