package com.example.contactmanagementapp

sealed interface ContactEvent{
    object SaveContact: ContactEvent
    data class SetFirstName(val firstName: String): ContactEvent
    data class SetLastName(val lastName: String): ContactEvent
    data class SetPhone(val phone: String): ContactEvent
    data class SetImage(val contact: Contact,  val image: ByteArray?): ContactEvent {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as SetImage

            if (contact != other.contact) return false
            if (image != null) {
                if (other.image == null) return false
                if (!image.contentEquals(other.image)) return false
            } else if (other.image != null) return false

            return true
        }

        override fun hashCode(): Int {
            var result = contact.hashCode()
            result = 31 * result + (image?.contentHashCode() ?: 0)
            return result
        }
    }

    object RestImage: ContactEvent
    data class SetEmail(val email: String): ContactEvent
    object GetContacts: ContactEvent
    data class DeleteContact(val contact: Contact): ContactEvent
    object ShowAddContactDialog: ContactEvent
    object HideAddContactDialog: ContactEvent
    object ShowExportContact: ContactEvent
    object HideExportContact: ContactEvent
    data class UpdateFirstName(val contact: Contact, val firstName: String): ContactEvent
    data class UpdateLastName(val contact: Contact, val lastName: String): ContactEvent
    data class UpdatePhone(val contact: Contact, val phone: String): ContactEvent
    data class UpdateEmail(val contact: Contact, val email: String): ContactEvent

    class ShowCamera(val contact: Contact) : ContactEvent
}
