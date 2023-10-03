package com.example.contactmanagementapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContactDialogue(
    state: ContactState,
    onEvent: (ContactEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {onEvent(ContactEvent.HideDialog)},
        title = { Text(text = "Add Contact") },
        confirmButton = {
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd){
                            Button(onClick = {
                                onEvent(ContactEvent.SaveContact)
                            }) {
                                Text(text = "Save")
                            }
                        }
        },
        text = {
            Column {
                TextField(value = state.firstName, onValueChange = {
                    onEvent(ContactEvent.SetFirstName(it))
                }, placeholder = { Text(text = "First Name") })
                TextField(value = state.lastName, onValueChange = {
                    onEvent(ContactEvent.SetLastName(it))
                }, placeholder = { Text(text = "Last Name") })
                TextField(value = state.phone, onValueChange = {
                    onEvent(ContactEvent.SetPhone(it))
                }, placeholder = { Text(text = "Phone Number") })
            }
        },
    )

}