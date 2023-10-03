package com.example.contactmanagementapp

data class ContactState (
    val contacts:List<Contact> = emptyList(),
    val firstName:String = "",
    val lastName:String = "",
    val phone:String = "",
    val image:Int = 0,
    val email:String = "",
    val showDialog:Boolean = false
) {
}