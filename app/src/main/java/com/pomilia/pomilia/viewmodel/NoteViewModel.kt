package com.pomilia.pomilia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pomilia.pomilia.model.Note

class NoteViewModel : ViewModel() {
    private var nextId = 3

    private val _notes = MutableLiveData<List<Note>>(
        listOf(
            Note(
                1,
                "Analisi 1",
                "Limiti e derivate",
                "Appunti",
                "Matematica"
            ),
            Note(
                2,
                "Programmazione",
                "Introduzione a Kotlin",
                "Riassunti",
                "Informatica"
            )
        )
    )

    fun addNote(
        title: String,
        content: String,
        category: String,
        subject: String
    ) {

        val currentList = _notes.value?.toMutableList() ?: mutableListOf()

        currentList.add(
            Note(
                id = nextId++,
                title = title,
                content = content,
                category = category,
                subject = subject
            )
        )

        _notes.value = currentList
    }

    // Funzione aggiunta per eliminare una nota dalla lista temporanea
    fun deleteNote(note: Note) {
        // 1. Recuperiamo la lista corrente come MutableList
        val currentList = _notes.value?.toMutableList() ?: mutableListOf()

        // 2. Rimuoviamo la nota passata come parametro
        currentList.remove(note)

        // 3. Notifichiamo gli osservatori (il Fragment) aggiornando il LiveData
        _notes.value = currentList
    }
    val notes: LiveData<List<Note>> = _notes
}