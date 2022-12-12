package com.mikkelthygesen.billsplit.ui.features.main.widgets.widgets

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.models.Person
import com.mikkelthygesen.billsplit.ui.features.main.MainViewModel
import com.mikkelthygesen.billsplit.ui.features.main.profile.widget.shadowModifier
import com.mikkelthygesen.billsplit.ui.widgets.CircularUrlImageView
import com.mikkelthygesen.billsplit.ui.widgets.LoadingView

@Composable
fun ProfilePictureWithUpload(
    user: Person,
    mainViewModel: MainViewModel = viewModel()
) {
    var selectedImage by remember {
        mutableStateOf<Uri?>(null)
    }
    var uploadingImage by remember {
        mutableStateOf(false)
    }
    var showConfirmDialog by remember {
        mutableStateOf(false)
    }

    if (showConfirmDialog) {
        ConfirmPictureDialog(
            uri = selectedImage!!,
            onDismiss = {
                showConfirmDialog = false
            }) {
            uploadingImage = true
            showConfirmDialog = false
            mainViewModel.uploadProfilePhoto(
                selectedImage!!,
                onSuccess = {
                    selectedImage = null
                    uploadingImage = false
                },
            )
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.also {
            selectedImage = it
            showConfirmDialog = true
        }
    }

    if (uploadingImage)
        LoadingView()
    else
        CircularUrlImageView(
            modifier = Modifier
                .clickable {
                    launcher.launch("image/*")
                }
                .padding(top = 32.dp)
                .size(100.dp),
            imageUrl = user.pfpUrlState
        )
}

@Composable
private fun ConfirmPictureDialog(uri: Uri, onDismiss: () -> Unit, onConfirm: () -> Unit) {

    Dialog(onDismissRequest = onDismiss) {
        Column(
            shadowModifier(MaterialTheme.colors.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularUrlImageView(imageUrl = uri.toString())
            Button(onClick = onConfirm) {
                Text(text = "Confirm")
            }
        }
    }
}