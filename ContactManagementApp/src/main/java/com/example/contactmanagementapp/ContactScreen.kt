package com.example.contactmanagementapp

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

//var contacts = listOf<Contact>(
//    Contact("John", " Doe", "1234567890", R.drawable.ic_launcher_background),
//    Contact("Jane ", "Doe", "1234567890", R.drawable.ic_launcher_background),
//    Contact("John ", "Smith", "1234567890", R.drawable.ic_launcher_background),
//    Contact("Jane ", "Smith", "1234567890", R.drawable.ic_launcher_background),
//    Contact("John ", "Johnson", "1234567890", R.drawable.ic_launcher_background),
//    Contact("Jane ", "Johnson", "1234567890", R.drawable.ic_launcher_background),
//    Contact("John ", "Williams", "1234567890", R.drawable.ic_launcher_background),
//    Contact("Jane ", "Williams", "1234567890", R.drawable.ic_launcher_background),
//    Contact("John ", "Jones", "1234567890", R.drawable.ic_launcher_background),
//    Contact("Jane ", "Jones", "1234567890", R.drawable.ic_launcher_background),
//    Contact("John ", "Brown", "1234567890", R.drawable.ic_launcher_background),
//    Contact("Jane ", "Brown", "1234567890", R.drawable.ic_launcher_background),
//    Contact("John ", "Davis", "1234567890", R.drawable.ic_launcher_background),
//    Contact("Jane ", "Davis", "1234567890", R.drawable.ic_launcher_background),
//    Contact("John ", "Miller", "1234567890", R.drawable.ic_launcher_background),
//    Contact("Jane ", "Miller", "1234567890", R.drawable.ic_launcher_background),
//    Contact("John ", "Wilson", "1234567890", R.drawable.ic_launcher_background),
//    Contact("Jane ", "Wilson", "1234567890", R.drawable.ic_launcher_background),
//    Contact("John ", "Moore", "1234567890", R.drawable.ic_launcher_background),
//    Contact("Jane ", "Moore", "1234567890", R.drawable.ic_launcher_background),
//)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ContactScreen(state: ContactState, onEvent: (ContactEvent) -> Unit) {
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    Scaffold(floatingActionButton = {
        Column() {
            if (!state.showCamera) {

                ExtendedFloatingActionButton(onClick = {
                    onEvent(ContactEvent.ShowAddContactDialog)
                }, modifier = Modifier.padding(12.dp)) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                    )
                    Text(text = "Add Contact")
                }
                ExtendedFloatingActionButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                    )
                    Text(text = "Import Contact")

                }
            }
        }
    }) { padding ->
        if (state.showDialog) {
            AddContactDialogue(state, onEvent)
        }
        if (state.showCamera) {
            if (cameraPermissionState.status.isGranted) {
                CameraScreen(state, onEvent)
            } else {
                cameraPermissionState.launchPermissionRequest()
            }
        } else {
            Column {
                ContactList(modifier = Modifier, padding = padding, state, onEvent)
            }
        }

    }
}

@Composable
fun ContactList(
    modifier: Modifier,
    padding: PaddingValues,
    state: ContactState,
    onEvent: (ContactEvent) -> Unit
) {
    onEvent(ContactEvent.GetContacts)
    LazyColumn(modifier = modifier.fillMaxWidth(), contentPadding = padding) {
        items(state.contacts) { contact ->
            ContactCard(contact = contact,state.contacts, onEvent)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ContactCard(contact: Contact, contacts: List<Contact>, onEvent: (ContactEvent) -> Unit) {
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    val resource = painterResource(id = R.drawable.ic_launcher_background)
    val drawable = remember { mutableStateOf(resource) }
    if (contact.image != null) {
        //TODO: maybe error here
        val bitmap = BitmapFactory.decodeByteArray(contact.image, 0, contact.image!!.size)
        drawable.value = BitmapPainter(bitmap.asImageBitmap())
    }
    Row {
        var firstName by remember { mutableStateOf(contact.firstName) }
        var lastName by remember { mutableStateOf(contact.lastName) }
        var phone by remember { mutableStateOf(contact.phone) }
        var email by remember { mutableStateOf(contact.email) }
        Image(
            painter = drawable.value,
            contentDescription = null,
            modifier = Modifier
                .padding(8.dp)
                .weight(0.2f)
                .fillMaxSize()
                .clickable {
                    if (cameraPermissionState.status.isGranted) {
                        onEvent(ContactEvent.ShowCamera(contact))
                    } else {
//                        onEvent(ContactEvent.SetImage(contact, state.image))
                        cameraPermissionState.launchPermissionRequest()
                    }
                },
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .weight(0.8f)
        ) {
            TextField(
                value = firstName,
                onValueChange = {
                    // check if the changed value is already in the list
                    if (contacts.any { contact -> contact.firstName == it }) {
                        firstName = contact.firstName
                    } else {
                        firstName = it
                        onEvent(ContactEvent.UpdateFirstName(contact, it))
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Black)
            )
            TextField(
                value = lastName,
                onValueChange = {
                    // check if the changed value is already in the list
                    if (contacts.any { contact -> contact.lastName == it }) {
                        lastName = contact.lastName
                    } else {
                        lastName = it
                        onEvent(ContactEvent.UpdateLastName(contact, it))
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Black)
            )
            TextField(
                value = phone,
                onValueChange = {
                    phone = it
                    onEvent(ContactEvent.UpdatePhone(contact, it))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Black)
            )
            TextField(
                value = email,
                onValueChange = {
                    email = it
                    onEvent(ContactEvent.UpdateEmail(contact, it))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Black)
            )
        }
        IconButton(onClick = { onEvent(ContactEvent.DeleteContact(contact)) }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
            )
        }

    }
}






