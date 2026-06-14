package com.pomilia.pomilia.viewmodel

import android.app.Application
import android.se.omapi.Session
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.pomilia.pomilia.data.local.NoteEntity
import com.pomilia.pomilia.data.local.PomiliaDatabase
import com.pomilia.pomilia.data.repository.NoteRepository
import com.pomilia.pomilia.security.SessionManager
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NoteRepository

    val notes: LiveData<List<NoteEntity>>

    init {
        val noteDao = PomiliaDatabase
            .getDatabase(application)
            .noteDao()

        repository = NoteRepository(noteDao)

        notes = repository.notes
    }

    fun getNoteById(noteId: Int): LiveData<NoteEntity?>{
        return  repository.getNoteById(noteId)
    }
    fun addNote(
        title: String,
        content: String,
        category: String,
        subject: String
    ) {
        viewModelScope.launch {
            val sessionManager = SessionManager(getApplication())
            val username =sessionManager.getUsername() ?: "unknown_user"
            val note = NoteEntity(
                title = title,
                content = content,
                category = category,
                subject = subject,
                ownerUsername = username

            )

            repository.insertNote(note)
        }
    }

    fun updateNote(note: NoteEntity){
        viewModelScope.launch {
            repository.updateNote(note)
        }
    }
    fun deleteNote(note: NoteEntity) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }



}