package com.example.myapplication.models.note

import androidx.lifecycle.LiveData

class NoteRepository(private val noteDAO: NoteDAO) {
    val allNotes: LiveData<List<NoteEntity>> = noteDAO.getAll()
    suspend fun insert(note: NoteEntity) {
        noteDAO.insertAll(note)
    }
    suspend fun delete(note: NoteEntity) {
        noteDAO.delete(note)
    }
}