package com.mikkelthygesen.billsplit.features.main.widgets

 import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.domain.models.Person
import com.mikkelthygesen.billsplit.features.main.profile.ProfileViewModel
import com.mikkelthygesen.billsplit.ui.shadowModifier
import com.mikkelthygesen.billsplit.ui.widgets.CircularUrlImageView
import com.mikkelthygesen.billsplit.ui.widgets.LoadingView
import com.mikkelthygesen.billsplit.ui.widgets.ProfilePicture
import kotlinx.coroutines.launch

@Composable
fun ProfilePictureWithUpload(
    user: Person
) {
    val profileViewModel: ProfileViewModel = viewModel()

    var selectedImage by remember {
        mutableStateOf<Uri?>(null)
    }
    var uploadingImage by remember {
        mutableStateOf(false)
    }
    var showConfirmDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = Unit, block = {
        profileViewModel.uiEventsState.collect { event ->
            if (event is ProfileViewModel.ProfilePictureUploaded) {
                selectedImage = null
                uploadingImage = false
            }
        }
    })

    val coroutineScope = rememberCoroutineScope()

    if (showConfirmDialog) {
        ConfirmPictureDialog(
            uri = selectedImage!!,
            onDismiss = {
                showConfirmDialog = false
            }) {
            uploadingImage = true
            showConfirmDialog = false
            coroutineScope.launch {
                profileViewModel.uploadProfilePhoto(selectedImage!!)
            }
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
        ProfilePicture(
            modifier = Modifier
                .padding(vertical = 32.dp)
                .size(100.dp)
                .clip(CircleShape)
                .clickable {
                    launcher.launch("image/*")
                },
            person = user
        )
}

@Composable
private fun ConfirmPictureDialog(uri: Uri, onDismiss: () -> Unit, onConfirm: () -> Unit) {

    Dialog(onDismissRequest = onDismiss) {
        Column(
            Modifier.shadowModifier(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularUrlImageView(imageUrl = uri.toString())
            Button(onClick = onConfirm) {
                Text(text = "Confirm")
            }
        }
    }
}