package com.example.contactmanagementapp

sealed interface ContactEvent{
    object SaveContact: ContactEvent
    data class SetFirstName(val firstName: String): ContactEvent
    data class SetLastName(val lastName: String): ContactEvent
    data class SetPhone(val phone: String): ContactEvent
    data class SetImage(val image: Int): ContactEvent
    data class SetEmail(val email: String): ContactEvent
    object GetContacts: ContactEvent
    data class DeleteContact(val contact: Contact): ContactEvent
    object ShowDialog: ContactEvent
    object HideDialog: ContactEvent
}
