package com.example.contactmanagementapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room


@Suppress("UNCHECKED_CAST")
class MainActivity : ComponentActivity() {

    private val _db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "appdatabase.db"
        ).build()
    }
    private val viewModel by viewModels<ContactViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ContactViewModel(_db.contactDao) as T
                }
            }
        },
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Box {
                val state by viewModel.state.collectAsState()
                ContactScreen(state = state, onEvent = {
                    viewModel.onEvent(applicationContext, it)
                })
            }
        }
    }
}


