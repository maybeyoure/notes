package com.example.myapplication.models.note

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp


@Entity(tableName = "notes")
data class NoteEntity(
    var title: String,
    var text: String,
    var color: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var created: Long = Timestamp(System.currentTimeMillis()).time
}