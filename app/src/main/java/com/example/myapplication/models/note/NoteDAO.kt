package com.example.myapplication.models.note

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface NoteDAO {
    @Insert
    fun insertAll(vararg notes: NoteEntity)

    @Delete
    fun delete(note: NoteEntity)

    @Update
    fun update(note: NoteEntity)

    @Query("SELECT * FROM notes ORDER BY created DESC")
    fun getAll(): List<NoteEntity>

    @Query("SELECT * FROM notes WHERE id = :id")
    fun get(id: Int): NoteEntity?
}
