package com.example.contactmanagementapp

data class ContactState(
    val contacts: List<Contact> = emptyList(),
    val firstName: String = "",
    val lastName: String = "",
    val phone: String = "",
    val image: ByteArray? = null,
    val email: String = "",
    val showDialog: Boolean = false,
    val showCamera: Boolean = false,
    val pictureTaken: Boolean = false,
    val currentContact: Contact? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ContactState

        if (contacts != other.contacts) return false
        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false
        if (phone != other.phone) return false
        if (image != null) {
            if (other.image == null) return false
            if (!image.contentEquals(other.image)) return false
        } else if (other.image != null) return false
        if (email != other.email) return false
        if (showDialog != other.showDialog) return false
        if (showCamera != other.showCamera) return false

        return true
    }

    override fun hashCode(): Int {
        var result = contacts.hashCode()
        result = 31 * result + firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        result = 31 * result + phone.hashCode()
        result = 31 * result + (image?.contentHashCode() ?: 0)
        result = 31 * result + email.hashCode()
        result = 31 * result + showDialog.hashCode()
        result = 31 * result + showCamera.hashCode()
        return result
    }
}