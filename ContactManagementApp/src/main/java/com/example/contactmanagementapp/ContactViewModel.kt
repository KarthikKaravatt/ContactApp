package com.example.contactmanagementapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ContactViewModel(private val doa: ContactDao) : ViewModel() {
    private val _state = MutableStateFlow(ContactState())
    private val _contacts = MutableStateFlow(emptyList<Contact>())
    val state = combine(_state, _contacts) { state, contacts ->
        state.copy(contacts = contacts)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ContactState())

    fun onEvent(event: ContactEvent) {
        when (event) {
            is ContactEvent.SaveContact -> {
                val firstName = _state.value.firstName
                val lastName = _state.value.lastName
                val phone = _state.value.phone
                val image = _state.value.image
                val email = _state.value.email
                viewModelScope.launch {
                    doa.insertAll(Contact(firstName, lastName, phone, email, image))
                    _contacts.update { doa.getAll() }
                }
                _state.update {
                    it.copy(
                        showDialog = false,
                        firstName = "",
                        lastName = "",
                        phone = "",
                        image = 0,
                        email = ""
                    )
                }
            }

            is ContactEvent.SetFirstName -> {
                _state.update {
                    it.copy(firstName = event.firstName)
                }
            }

            is ContactEvent.SetLastName -> {
                _state.update {
                    it.copy(lastName = event.lastName)
                }
            }

            is ContactEvent.SetPhone -> {
                _state.update {
                    if (event.phone.length > 10) {
                        return@update it.copy(phone = event.phone.substring(0, 10))
                    }
                    // check if the number contains a letter
                    else if (event.phone.contains(Regex("[a-zA-Z]"))) {
                        return@update it.copy(
                            phone = event.phone.substring(
                                0,
                                event.phone.length - 1
                            )
                        )
                    }
                    it.copy(phone = event.phone)
                }
            }

            is ContactEvent.SetImage -> {
                _state.update {
                    it.copy(image = event.image)
                }
            }

            is ContactEvent.SetEmail -> {
                _state.update {
                    it.copy(email = event.email)
                }
            }

            is ContactEvent.GetContacts -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(contacts = doa.getAll())
                    }
                    _contacts.update { doa.getAll() }
                }
            }

            is ContactEvent.DeleteContact -> {
                viewModelScope.launch {
                    doa.delete(event.contact)
                    _contacts.update { doa.getAll() }
                }
            }

            is ContactEvent.ShowDialog -> {
                _state.update {
                    it.copy(showDialog = true)
                }
            }

            is ContactEvent.HideDialog -> {
                _state.update {
                    it.copy(showDialog = false)
                }
            }
        }
    }
}