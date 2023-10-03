package com.example.contactmanagementapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(state: ContactState, onEvent: (ContactEvent) -> Unit) {
    Scaffold(floatingActionButton = {
        ExtendedFloatingActionButton(onClick = {
            onEvent(ContactEvent.ShowDialog)
        }) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,

                )
            Text(text = "Add Contact")
        }
    }) { padding ->
        if (state.showDialog) {
            AddContactDialogue(state, onEvent)
        }
        Column {
            ContactList(modifier = Modifier, padding = padding, state, onEvent)
        }

    }
}

@Composable
fun ContactList(modifier: Modifier, padding: PaddingValues, state: ContactState, onEvent: (ContactEvent) -> Unit) {
    BoxWithConstraints(modifier = modifier) {
        LazyColumn(modifier = modifier.fillMaxWidth(), contentPadding = padding) {
            items(state.contacts) { contact ->
                ContactCard(contact = contact, onEvent)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactCard(contact: Contact, onEvent: (ContactEvent) -> Unit) {
    Row {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = null, modifier = Modifier
                .padding(8.dp)
                .weight(0.2f)
                .fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .weight(0.8f)
        ) {
            TextField(
                value = contact.firstName.plus("".plus(contact.lastName)),
                readOnly = true,
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Black)
            )
            TextField(
                value = contact.phone, readOnly = true, onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Black)
            )
        }
        IconButton(onClick = {onEvent(ContactEvent.DeleteContact(contact))}) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
            )
        }

    }
}





