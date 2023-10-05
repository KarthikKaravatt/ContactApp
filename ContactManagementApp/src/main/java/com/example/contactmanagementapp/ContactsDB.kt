package com.example.contactmanagementapp

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase

@Entity(tableName = "contacts", primaryKeys = ["firstName", "lastName"])
data class Contact(
    val firstName: String,
    val lastName: String,
    val phone: String,
    val email: String,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB) var image: ByteArray?
) {
    // note sure why but it wanted me to generated this
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Contact

        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false
        if (phone != other.phone) return false
        if (email != other.email) return false
        if (image != null) {
            if (other.image == null) return false
            if (!image.contentEquals(other.image)) return false
        } else if (other.image != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        result = 31 * result + phone.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + (image?.contentHashCode() ?: 0)
        return result
    }
}

@Dao
interface ContactDao {
    @Query("SELECT * FROM contacts")
    suspend fun getAll(): List<Contact>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg contacts: Contact)

    @Delete
    suspend fun delete(contact: Contact)


    //Update the first name
    @Query("UPDATE contacts SET firstName = :firstName WHERE firstName = :oldFirstName AND lastName = :lastName")
    suspend fun updateFirstName(firstName: String, oldFirstName: String, lastName: String)

    //Update the last name
    @Query("UPDATE contacts SET lastName = :lastName WHERE firstName = :firstName AND lastName = :oldLastName")
    suspend fun updateLastName(firstName: String, lastName: String, oldLastName: String)

    //Update the phone number
    @Query("UPDATE contacts SET phone = :phone WHERE firstName = :firstName AND lastName = :lastName")
    suspend fun updatePhone(firstName: String, lastName: String, phone: String)

    //Update the email
    @Query("UPDATE contacts SET email = :email WHERE firstName = :firstName AND lastName = :lastName")
    suspend fun updateEmail(firstName: String, lastName: String, email: String)

    //Update the image
    @Query("UPDATE contacts SET image = :image WHERE firstName = :firstName AND lastName = :lastName")
    suspend fun updateImage(firstName: String, lastName: String, image: ByteArray)

}

@Database(entities = [Contact::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract val contactDao: ContactDao
}