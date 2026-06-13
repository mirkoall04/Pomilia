package com.pomilia.pomilia.data.repository

import androidx.lifecycle.LiveData
import com.pomilia.pomilia.data.local.NoteDao
import com.pomilia.pomilia.data.local.NoteEntity

class NoteRepository(private val noteDao: NoteDao) {
    val notes: LiveData<List<NoteEntity>> = noteDao.getAllNotes()

    fun getNoteById(noteId: Int): LiveData<NoteEntity?>{
        return noteDao.getNoteById(noteId)
    }

    suspend fun insertNote(note: NoteEntity){
        noteDao.insertNote(note)
    }

    suspend fun updateNote(note: NoteEntity){
        noteDao.updateNote(note)
    }
    suspend fun deleteNote(note: NoteEntity){
        noteDao.deleteNote(note)
    }


}