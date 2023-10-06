package com.example.contactmanagementapp

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.database.Cursor
import android.provider.ContactsContract
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@SuppressLint("Range", "Recycle")
@Composable
fun ContactPickerTwinTurbo(
    selectedContact: MutableState<Contact?>
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickContact(),
        onResult = {
            val contentResolver: ContentResolver = context.contentResolver
            var firstName = ""
            var lastName = ""
            var number = ""
            var email = ""
            val cursor: Cursor? = contentResolver.query(it!!, null, null, null, null)
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    val name =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    Log.d("Name", name)
                    val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))

                    // Split the name into first and last names
                    val nameParts = name.split(" ")
                    if (nameParts.size > 1) {
                        firstName = nameParts[0]
                        lastName = nameParts[1]
                        Log.d("First Name", firstName)
                        Log.d("Last Name", lastName)
                    }

                    // Get the phone number
                    val phones: Cursor? = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                        null,
                        null
                    )
                    if (phones != null) {
                        while (phones.moveToNext()) {
                            number =
                                phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            Log.d("Number", number)
                        }
                        phones.close()
                    }

                    // Get the email
                    val emails: Cursor? = contentResolver.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + id,
                        null,
                        null
                    )
                    if (emails != null) {
                        while (emails.moveToNext()) {
                            email =
                                emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA))
                            Log.d("Email", email)
                        }
                        emails.close()
                    }
                }
            }

            // Create a Contact object and update the MutableState variable
            val contact = Contact(firstName, lastName, number, email, null)
            selectedContact.value = contact
        }
    )
    Button(
        onClick = {
            if (selectedContact.value == null) {
                launcher.launch()
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) { Text(text = "Pick Contact") }
}
