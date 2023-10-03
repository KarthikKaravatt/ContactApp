package com.example.contactmanagementapp

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
    val image: Int
)

@Dao
interface ContactDao {
    @Query("SELECT * FROM contacts")
    suspend fun getAll(): List<Contact>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg contacts: Contact)

    @Delete
    suspend fun delete(contact: Contact)
}

@Database(entities = [Contact::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract val contactDao: ContactDao
}